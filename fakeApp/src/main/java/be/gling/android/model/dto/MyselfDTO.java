package be.gling.android.model.dto;



import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by florian on 11/11/14.
 */
public class MyselfDTO extends AccountDTO  {

    private Boolean loginAccount;

    private Boolean facebookAccount;

    private String authenticationKey;

    private Long businessId;

    public MyselfDTO() {
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public Boolean getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(Boolean loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Boolean getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(Boolean facebookAccount) {
        this.facebookAccount = facebookAccount;
    }
}
