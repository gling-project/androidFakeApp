package be.gling.android.model.util.externalRequest;

import be.gling.android.model.dto.MyselfDTO;
import be.gling.android.model.dto.ResultDTO;
import be.gling.android.model.dto.TestFacebookDTO;
import be.gling.android.model.dto.post.CustomerRegistrationDTO;
import be.gling.android.model.dto.post.FacebookAuthenticationDTO;
import be.gling.android.model.dto.post.ForgotPasswordDTO;
import be.gling.android.model.dto.post.LoginDTO;
import be.gling.android.model.dto.technical.DTO;

/**
 * Created by florian on 11/11/14.
 * List the aviable request from the server
 */
public enum RequestEnum {

    //login, etc..
    LOGIN(
            RequestType.POST, false, "rest/login", LoginDTO.class, MyselfDTO.class),
    LOGIN_FACEBOOK(
            RequestType.GET, false, "rest/login/facebook/:token", null, MyselfDTO.class),
    FORGOT_PASSWORD(
            RequestType.PUT, false, "rest/forgot/password", ForgotPasswordDTO.class, ResultDTO.class),
    REGISTRATION(
            RequestType.POST, false, "rest/registration/customer", CustomerRegistrationDTO.class, MyselfDTO.class),
    FACEBOOK_TEST(
            RequestType.POST, false, "rest/facebook/test", FacebookAuthenticationDTO.class, TestFacebookDTO.class),
    GET_MYSELF(
            RequestType.GET, true, "rest/myself", null, MyselfDTO.class);


    private final RequestType requestType;
    private final boolean needAuthentication;
    private final String target;
    private final Class<? extends DTO> sentDTO;
    private final Class<? extends DTO> receivedDTO;

    private RequestEnum(RequestType requestType,
                        boolean needAuthentication,
                        String target,
                        Class<? extends DTO> sentDTO,
                        Class<? extends DTO> receivedDTO) {

        this.requestType = requestType;
        this.needAuthentication = needAuthentication;
        this.target = target;
        this.sentDTO = sentDTO;
        this.receivedDTO = receivedDTO;
    }


    public String getTarget() {
        return target;
    }

    public Class<? extends DTO> getSentDTO() {
        return sentDTO;
    }

    public Class<? extends DTO> getReceivedDTO() {
        return receivedDTO;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public boolean needAuthentication() {
        return needAuthentication;
    }

    public static enum RequestType {
        GET, POST, PUT, DELETE
    }
}
