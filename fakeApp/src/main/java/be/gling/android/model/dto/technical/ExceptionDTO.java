package be.gling.android.model.dto.technical;

/**
 * Created by florian on 11/11/14.
 */
public class ExceptionDTO extends DTO {

    private String message = null;

    public ExceptionDTO(String message) {
        this.message = message;
    }

    public ExceptionDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ExceptionDTO{" +
                "message='" + message + '\'' +
                '}';
    }
}
