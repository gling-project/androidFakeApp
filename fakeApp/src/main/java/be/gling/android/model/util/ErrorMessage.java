package be.gling.android.model.util;

/**
 * Created by florian on 11/11/14.
 * Error message (to translate)
 */
public enum ErrorMessage {

    WRONG_SENT_DTO("Sent dto expected {0} but found {1}"),
    WRONG_RECEIVED_DTO("Received dto expected {0} but found {1}"),
    NULL_ELEMENT("Expected {0} but this param is null"),
    UNEXPECTED_ERROR("Unexpected error : {0}");

    private final String message;

    private ErrorMessage(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
