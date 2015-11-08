package be.gling.android.model.dto.post;


import be.gling.android.R;
import be.gling.android.model.GenderEnum;
import be.gling.android.model.constant.ValidationRegex;
import be.gling.android.model.dto.LangDTO;
import be.gling.android.model.dto.technical.DTO;
import be.gling.android.model.util.annotation.NotNull;
import be.gling.android.model.util.annotation.Pattern;
import be.gling.android.model.util.annotation.Size;

/**
 * Created by florian on 11/11/14.
 */
public class AccountRegistrationDTO extends DTO {

    @NotNull
    private GenderEnum gender;

    @NotNull
    @Size(min = 2, max = 50, message = R.string.account_form_error_name)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 50, message = R.string.account_form_error_name)
    private String lastname;

    @NotNull
    @Pattern(regex = ValidationRegex.EMAIL, message = R.string.verification_email)
    private String email;

    @NotNull
    @Pattern(regex = "[a-zA-Z0-9-_]{6,18}", message = R.string.verification_password)
    private String password;

    private LangDTO lang;

    private Boolean keepSessionOpen;

    public AccountRegistrationDTO() {
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LangDTO getLang() {
        return lang;
    }

    public void setLang(LangDTO lang) {
        this.lang = lang;
    }

    public Boolean getKeepSessionOpen() {
        return keepSessionOpen;
    }

    public void setKeepSessionOpen(Boolean keepSessionOpen) {
        this.keepSessionOpen = keepSessionOpen;
    }

    @Override
    public String toString() {
        return "AccountRegistrationDTO{" +
                "gender=" + gender +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lang=" + lang +
                ", keepSessionOpen=" + keepSessionOpen +
                '}';
    }
}
