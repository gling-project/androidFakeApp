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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.gling.android.R;
import be.gling.android.model.AccountTypeEnum;
import be.gling.android.model.GenderEnum;
import be.gling.android.model.dto.LangDTO;
import be.gling.android.model.dto.MyselfDTO;
import be.gling.android.model.dto.TestFacebookDTO;
import be.gling.android.model.dto.post.AccountRegistrationDTO;
import be.gling.android.model.dto.post.CustomerRegistrationDTO;
import be.gling.android.model.dto.post.FacebookAuthenticationDTO;
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
import be.gling.android.view.widget.technical.FieldCheckBox;
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
                    new Field.FieldProperties(new FieldEditText(this), String.class, R.string.g_reapeat_password, new Field.FieldProperties.ErrorController() {
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
                    }), new Field.FieldProperties(new FieldCheckBox(this), Boolean.class, R.string.a_registration_accept_sla, new Field.FieldProperties.ErrorController() {
                @Override
                public Integer test(Object value) {
                    if (((Boolean) value) != true) {
                        return R.string.a_registration_error_sla_not_accepted;
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

                FacebookAuthenticationDTO facebookAuthenticationDTO = new FacebookAuthenticationDTO();
                facebookAuthenticationDTO.setToken(loginResult.getAccessToken().getToken());
                facebookAuthenticationDTO.setAccountType(AccountTypeEnum.CUSTOMER);
                facebookAuthenticationDTO.setUserId(loginResult.getAccessToken().getUserId());
                facebookAuthenticationDTO.setLang(new LangDTO("fr", "Français"));

                //test facebook login
                //create request
                WebClient<TestFacebookDTO> webClient = new WebClient<TestFacebookDTO>(RequestEnum.FACEBOOK_TEST,
                        facebookAuthenticationDTO,
                        TestFacebookDTO.class);

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
                        TestFacebookDTO testFacebookDTO = (TestFacebookDTO) successDTO;
                        switch (testFacebookDTO.getStatus()) {
                            case ALREADY_REGISTRERED:
                                RegistrationActivity.this.successAction(testFacebookDTO.getMyself());
                                break;
                            case ACCOUNT_WITH_SAME_EMAIL:
                                //TODO fusion
                                break;
                            case OK:

                                //assign value
                                form.getField(R.string.g_gender).setValue(testFacebookDTO.getGender());
                                form.getField(R.string.g_gender).setEnabled(testFacebookDTO.getGender() == null);
                                form.getField(R.string.g_firstName).setValue(testFacebookDTO.getFirstname());
                                form.getField(R.string.g_firstName).setEnabled(testFacebookDTO.getFirstname() == null);
                                form.getField(R.string.g_lastName).setValue(testFacebookDTO.getLastname());
                                form.getField(R.string.g_lastName).setEnabled(testFacebookDTO.getLastname() == null);
                                form.getField(R.string.g_email).setValue(testFacebookDTO.getEmail());
                                form.getField(R.string.g_gender).setEnabled(testFacebookDTO.getEmail() == null);

                                //remove field
                                form.getField(R.string.g_password).setVisibility(View.GONE);
                                form.getField(R.string.g_reapeat_password).setVisibility(View.GONE);
                                facebookLoginButton.setVisibility(View.GONE);
                                Dialog dialog;
                                if (testFacebookDTO.getGender() == null ||
                                        testFacebookDTO.getFirstname() == null ||
                                        testFacebookDTO.getLastname() == null ||
                                        testFacebookDTO.getEmail() == null) {
                                    dialog = DialogConstructor.dialogInfo(RegistrationActivity.this, R.string.a_registration_facebook_success_missing_data);
                                } else {
                                    dialog = DialogConstructor.dialogInfo(RegistrationActivity.this, R.string.a_registration_facebook_success);
                                }
                                dialog.show();

                                break;
                        }
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

                        //create customer dto
                        CustomerRegistrationDTO customerRegistrationDTO = new CustomerRegistrationDTO();
                        customerRegistrationDTO.setAccountRegistration((AccountRegistrationDTO) dto);

                        //create request
                        WebClient<MyselfDTO> webClient = new WebClient<MyselfDTO>(RequestEnum.REGISTRATION,
                                customerRegistrationDTO,
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
