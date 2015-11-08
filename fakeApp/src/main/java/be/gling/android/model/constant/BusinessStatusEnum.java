package be.gling.android.model.constant;

/**
 * Created by florian on 5/07/15.
 */
public enum BusinessStatusEnum {

    NOT_PUBLISHED("--.business.status.not_public"),
    WAITING_CONFIRMATION("--.business.status.waiting_confirmation"),
    PUBLISHED("--.business.status.published");

    private final String translationName;

    BusinessStatusEnum(String translationName) {
        this.translationName = translationName;
    }

    public String getTranslationName() {
        return translationName;
    }
}
