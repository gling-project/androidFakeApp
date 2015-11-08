package be.gling.android.model.constant;

/**
 * Created by florian on 11/11/14.
 */
public class ValidationRegex {

    public static final String EMAIL         = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String EMAIL_OR_NULL = "^($|[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$)";
    public static final String PASSWORD      = "^[a-zA-Z0-9_-]{6,18}$";

    public static final String PHONE       = "^[0-9. *-+/]{6,16}$";
    public static final String URL         = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$";
    public static final String URL_OR_NULL = "^($|(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]$)";
    public static final String VTA         = "^[a-zA-Z0-9\\.\\- ]{6,20}$";
    public static final String VAT         = "^[a-zA-Z0-9\\.\\- ]{6,20}$";
}
