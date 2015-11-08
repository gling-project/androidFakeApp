package be.gling.android.model.dto.post;

import be.gling.android.R;
import be.gling.android.model.dto.technical.DTO;
import be.gling.android.model.util.annotation.NotNull;
import be.gling.android.model.util.annotation.Pattern;

/**
 * Created by florian on 4/03/15.
 */
public class ForgotPasswordDTO extends DTO{

    @NotNull
    @Pattern(regex = Pattern.EMAIL,message = R.string.verification_email)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ForgotPasswordDTO{" +
                "email='" + email + '\'' +
                '}';
    }
}
