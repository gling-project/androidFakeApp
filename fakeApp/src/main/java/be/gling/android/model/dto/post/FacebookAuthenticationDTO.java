package be.gling.android.model.dto.post;


import be.gling.android.model.AccountTypeEnum;
import be.gling.android.model.dto.LangDTO;
import be.gling.android.model.dto.technical.DTO;
import be.gling.android.model.util.annotation.NotNull;

/**
 * Created by florian on 3/05/15.
 */
public class FacebookAuthenticationDTO extends DTO {

    private String userId;

    @NotNull
    private String token;

    private LangDTO lang;

    private AccountTypeEnum accountType;

    public FacebookAuthenticationDTO() {
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "FacebookAuthenticationDTO{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }

    public LangDTO getLang() {
        return lang;
    }

    public void setLang(LangDTO lang) {
        this.lang = lang;
    }
}
