package be.gling.android.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import be.gling.android.R;
import be.gling.android.model.GenderEnum;
import be.gling.android.model.dto.MyselfDTO;
import be.gling.android.model.dto.post.AccountRegistrationDTO;
import be.gling.android.model.dto.technical.DTO;
import be.gling.android.model.util.Storage;
import be.gling.android.model.util.exception.MyException;
import be.gling.android.model.util.externalRequest.Request;
import be.gling.android.model.util.externalRequest.RequestEnum;
import be.gling.android.model.util.externalRequest.WebClient;
import be.gling.android.view.RequestActionInterface;
import be.gling.android.view.activity.technical.AbstractActivity;
import be.gling.android.view.dialog.DialogConstructor;
import be.gling.android.view.widget.technical.Field;
import be.gling.android.view.widget.technical.FieldEditText;
import be.gling.android.view.widget.technical.FieldSelect;
import be.gling.android.view.widget.technical.Form;

/**
 * Created by florian on 8/11/15.
 */
public class RegistrationActivity extends AbstractActivity implements RequestActionInterface {

    private Form form = null;
    private Dialog loadingDialog;
    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        loadingDialog = DialogConstructor.dialogLoading(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_registration);

        //registration form
        final AccountRegistrationDTO accountRegistrationDTO = new AccountRegistrationDTO();
        try {
            form = new Form(this, accountRegistrationDTO,
                    new Field.FieldProperties(AccountRegistrationDTO.class.getDeclaredField("gender"), R.string.g_gender, new FieldSelect<GenderEnum>(this, R.array.gender_array, Arrays.asList(GenderEnum.MALE, GenderEnum.FEMALE))),
                    new Field.FieldProperties(AccountRegistrationDTO.class.getDeclaredField("firstname"), R.string.g_firstName),
                    new Field.FieldProperties(AccountRegistrationDTO.class.getDeclaredField("lastname"), R.string.g_lastName),
                    new Field.FieldProperties(AccountRegistrationDTO.class.getDeclaredField("email"), R.string.g_email, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS),
                    new Field.FieldProperties(AccountRegistrationDTO.class.getDeclaredField("password"), R.string.g_password, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD),
                    new Field.FieldProperties(new FieldEditText(this,InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD), String.class, R.string.g_reapeat_password, new Field.FieldProperties.ErrorController() {
                        @Override
                        public Integer test(Object value) {
                            try {
                                String password = (String) form.getField(R.string.g_password).getValue();
                                if (password == null || !password.equals(value)) {
                                    return R.string.g_error_passwords_not_equals;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
        insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

        //facebook login button
        final LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.login_button);
        facebookLoginButton.setReadPermissions("user_friends");
        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                //test facebook login
                //create request
                WebClient<MyselfDTO> webClient = new WebClient<MyselfDTO>(RequestEnum.LOGIN_FACEBOOK,
                        MyselfDTO.class);

                webClient.setParams("access_token", loginResult.getAccessToken().getToken());
                webClient.setParams("user_id", loginResult.getAccessToken().getUserId());

                //result
                Request request = new Request(new RequestActionInterface() {
                    @Override
                    public void displayErrorMessage(String errorMessage) {
                        findViewById(R.id.error_message_container).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.error_message)).setText(errorMessage);
                    }

                    @Override
                    public void loadingAction(boolean loading) {
                        form.setEnabled(!loading);
                        if (loading) {
                            loadingDialog.show();

                            // create animation and add to the refresh item
                            findViewById(R.id.error_message_container).setVisibility(View.GONE);
                        } else {
                            loadingDialog.cancel();
                        }
                    }

                    @Override
                    public void successAction(DTO successDTO) {
                        MyselfDTO myselfDTO = (MyselfDTO) successDTO;
                        RegistrationActivity.this.successAction(myselfDTO);
                    }
                }, webClient);

                //execute request
                request.execute();
            }

            @Override
            public void onCancel() {
                //TODO
            }

            @Override
            public void onError(FacebookException error) {
                //TODO
            }
        });

        // action
        findViewById(R.id.b_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    DTO dto = form.control();

                    if (dto != null) {

                        //create request
                        WebClient<MyselfDTO> webClient = new WebClient<MyselfDTO>(RequestEnum.REGISTRATION,
                                dto,
                                MyselfDTO.class);

                        Request request = new Request(RegistrationActivity.this, webClient);

                        //execute request
                        request.execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        form.saveToInstanceState(savedInstanceState);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        form.restoreFromInstanceState(savedInstanceState);
    }

    @Override
    public void displayErrorMessage(String errorMessage) {
        findViewById(R.id.error_message_container).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.error_message)).setText(errorMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loadingAction(boolean loading) {

        form.setEnabled(!loading);
        if (loading) {
            loadingDialog.show();

            // create animation and add to the refresh item
            findViewById(R.id.error_message_container).setVisibility(View.GONE);
        } else {
            loadingDialog.cancel();
        }
    }

    @Override
    public void successAction(DTO successDTO) {

        Storage.store(this, (MyselfDTO) successDTO);
        Intent intent = new Intent(RegistrationActivity.this, MAIN_ACTIVITY);
        //startActivityForResult(intent,0);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
