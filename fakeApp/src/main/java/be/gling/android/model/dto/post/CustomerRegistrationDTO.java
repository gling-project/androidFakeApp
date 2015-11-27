package be.gling.android.model.dto.post;

import java.util.ArrayList;

import be.gling.android.model.dto.technical.DTO;


/**
 * Created by florian on 11/11/14.
 */
public class CustomerRegistrationDTO extends DTO {

    private FacebookAuthenticationDTO facebookAuthentication;

    private AccountRegistrationDTO accountRegistration;


    public FacebookAuthenticationDTO getFacebookAuthentication() {
        return facebookAuthentication;
    }

    public void setFacebookAuthentication(FacebookAuthenticationDTO facebookAuthentication) {
        this.facebookAuthentication = facebookAuthentication;
    }

    public AccountRegistrationDTO getAccountRegistration() {
        return accountRegistration;
    }

    public void setAccountRegistration(AccountRegistrationDTO accountRegistration) {
        this.accountRegistration = accountRegistration;
    }

    @Override
    public String toString() {
        return "CustomerRegistrationDTO{" +
                "facebookAuthentication=" + facebookAuthentication +
                ", accountRegistration=" + accountRegistration +
                '}';
    }
}
