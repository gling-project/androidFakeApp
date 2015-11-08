package be.gling.android.model.dto;

import be.gling.android.R;
import be.gling.android.model.GenderEnum;
import be.gling.android.model.constant.ValidationRegex;
import be.gling.android.model.dto.technical.DTO;
import be.gling.android.model.util.annotation.NotNull;
import be.gling.android.model.util.annotation.Pattern;
import be.gling.android.model.util.annotation.Size;

/**
 * Created by florian on 11/11/14.
 */
public class AccountDTO extends DTO {

    private Long id;

    @NotNull
    protected GenderEnum gender;

    @NotNull
    @Size(min = 2, max = 50, message = R.string.account_form_error_name)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 50, message = R.string.account_form_error_name)
    private String lastname;

    @NotNull
    @Pattern(regex = ValidationRegex.EMAIL, message = R.string.verification_email)
    private String email;

    private LangDTO lang;

    public AccountDTO() {
    }


    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public LangDTO getLang() {
        return lang;
    }

    public void setLang(LangDTO lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", gender=" + gender +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", lang=" + lang +
                '}';
    }
}
