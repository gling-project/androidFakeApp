package be.gling.android.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import be.gling.android.R;
import be.gling.android.model.dto.ResultDTO;
import be.gling.android.model.dto.post.ForgotPasswordDTO;
import be.gling.android.model.dto.post.LoginDTO;
import be.gling.android.model.dto.technical.DTO;
import be.gling.android.model.util.externalRequest.Request;
import be.gling.android.model.util.externalRequest.RequestEnum;
import be.gling.android.model.util.externalRequest.WebClient;
import be.gling.android.view.RequestActionInterface;
import be.gling.android.view.activity.technical.AbstractActivity;
import be.gling.android.view.dialog.DialogConstructor;
import be.gling.android.view.util.Tools;
import be.gling.android.view.widget.technical.Field;
import be.gling.android.view.widget.technical.Form;

public class ForgotPasswordActivity extends AbstractActivity implements RequestActionInterface {

    private Form form = null;
    private Dialog loadingDialog;
    private String emailUsed;

    /**
     * build
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        loadingDialog = DialogConstructor.dialogLoading(this);

        setContentView(R.layout.activity_forgot_password);

        try {

            form = new Form(this, new ForgotPasswordDTO(),
                    new Field.FieldProperties(LoginDTO.class.getDeclaredField("email"), R.string.g_email,
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));


            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
            insertPoint.addView(form, ViewGroup.LayoutParams.WRAP_CONTENT);

            // login action
            findViewById(R.id.b_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ForgotPasswordDTO dto = null;
                    try {
                        dto = (ForgotPasswordDTO) form.control();

                        if (dto != null) {

                            emailUsed = ((ForgotPasswordDTO)dto).getEmail();

                            //create request
                            WebClient<ResultDTO> webClient = new WebClient<ResultDTO>(
//                                    ForgotPasswordActivity.this,
                                    RequestEnum.FORGOT_PASSWORD,
                                    dto,
                                    ResultDTO.class);

                            Request request = new Request(ForgotPasswordActivity.this, webClient);

                            //execute request
                            request.execute();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //help
        ((TextView)findViewById(R.id.help)).setText(Tools.getHelp(this, R.string.help_forgot_password));
    }


    @Override
    public void displayErrorMessage(String errorMessage) {
        DialogConstructor.displayErrorMessage(this, errorMessage);
    }

    @Override
    public void loadingAction(boolean loading) {

        form.setEnabled(!loading);
        if (loading) {
            loadingDialog.show();

            // create animation and add to the refresh item
        } else {
            loadingDialog.cancel();
        }
    }

    @Override
    public void successAction(DTO successDTO) {

        Toast.makeText(this,R.string.forgot_password_success,Toast.LENGTH_LONG).show();

        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_EMAIL,emailUsed);

        startActivity(intent);
    }
}
