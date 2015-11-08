package be.gling.android.model.util.externalRequest;

import android.util.Log;

import com.google.gson.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import be.gling.android.model.dto.technical.DTO;
import be.gling.android.model.dto.technical.ExceptionDTO;
import be.gling.android.model.service.ErrorMessageService;
import be.gling.android.model.util.ErrorMessage;
import be.gling.android.model.util.Storage;
import be.gling.android.model.util.exception.MyException;


/**
 * Created by florian on 11/11/14.
 * Communicate with the serveur
 */
public class WebClient<U extends DTO> {

    //main url of the service
    //public final static String TARGET_URL = "http://192.168.1.4:9000/";
    //main url of the service - office
    //public final static String TARGET_URL = "http://192.168.18.190:9000/";
    //  test
    //public final static String TARGET_URL = "http://roommate-service.herokuapp.com/";
    //  official
    public final static String TARGET_URL = "https://lynk-test.herokuapp.com/";

    //error message service
    private ErrorMessageService errorMessageService = new ErrorMessageService();


    private final RequestEnum request;
    private Class<U> expectedResult;
    private DTO dto;
    private Map<String, String> params = new HashMap<String, String>();

    public WebClient(RequestEnum request, Class<U> expectedResult) {
        this.request = request;
        this.expectedResult = expectedResult;
    }

    public WebClient(RequestEnum request, DTO dto, Class<U> expectedResult) {
        this.request = request;
        this.dto = dto;
        this.expectedResult = expectedResult;
    }

    public void setParams(String key, String value) {
        params.put(key, value);
    }

    /**
     * build, send and manage a http request.
     * Build the request by parameters of the request get
     */
    public U sendRequest() throws MyException {//RequestEnum request, DTO dto, Long id, Class<U> expectedResult) throws MyException {

        Log.w("webclient", "request :  " + request);

        //control request
        if (request == null) {
            throw new MyException(errorMessageService.getMessage(ErrorMessage.NULL_ELEMENT, "request"));
        }

        //control entrance
        if (request.getSentDTO() != null && !dto.getClass().equals(request.getSentDTO())) {
            throw new MyException(errorMessageService.getMessage(ErrorMessage.WRONG_SENT_DTO, dto.getClass(), request.getSentDTO()));
        }

        //initialize Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        gsonBuilder
                //add adapter
                .create();
        Gson gson = gsonBuilder.create();

        //build url
        String urlString = TARGET_URL + request.getTarget();

        //add other params
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlString = urlString.replace(":" + entry.getKey(), entry.getValue());
        }


        //initialize http client
        HttpClient httpClient = new DefaultHttpClient();

        //build the url request
        try {
            //build the url by requested by
            final HttpRequestBase httpRequest;
            switch (request.getRequestType()) {
                case GET:
                    httpRequest = new HttpGet(urlString);
                    break;
                case POST:
                    httpRequest = new HttpPost(urlString);
                    break;
                case DELETE:
                    httpRequest = new HttpDelete(urlString);
                    break;
                case PUT:
                    httpRequest = new HttpPut(urlString);
                    break;
                default:
                    throw new MyException("request type not found");
            }

            //
            //add params
            //

            //add Dto
            if (dto != null) {
                if (httpRequest instanceof HttpEntityEnclosingRequestBase) {
                    String json = gson.toJson(dto);
                    StringEntity params = new StringEntity(json, "UTF-8");
                    ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(params);
                    httpRequest.addHeader("content-type", "application/json");
                } else {
                    throw new MyException("cannot add dto into request type " + request.getRequestType().toString());
                }
            }

            //add authentication if it's needed
            if (request.needAuthentication()) {
                if (Storage.getAuthenticationKey() == null) {
                    throw new MyException("You need to be connected for this request : " + request.getTarget());
                }
                httpRequest.setHeader("authenticationKey", Storage.getAuthenticationKey());
            }

            Log.w("webclient", "send request to " + urlString);

            //send request
            HttpResponse response = httpClient.execute(httpRequest);

            Log.w("webclient", "response : " + response.getStatusLine());
            Log.w("webclient", "response : " + response.getEntity().toString());

            if (response.getStatusLine().getStatusCode() == 200) {

                //receive response
                if (request.getReceivedDTO() != null) {

                    String jsonString = EntityUtils.toString(response.getEntity());

                    return gson.fromJson(jsonString, expectedResult);
                }
            } else {

                //error
                String jsonString = EntityUtils.toString(response.getEntity());
                Log.w("WebClient", "error with code " + response.getStatusLine().getStatusCode());


                try {
                    ExceptionDTO exception = gson.fromJson(jsonString, ExceptionDTO.class);
                    String message = exception.getMessage();
                    if (message == null || message.length() == 0) {
                        message = "Unknow error";
                    }
                    throw new MyException(message);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    throw new MyException(jsonString);
                }
            }

            return null;

            // handle response here...
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new MyException(errorMessageService.getMessage(ErrorMessage.UNEXPECTED_ERROR, ex.getMessage()));
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
