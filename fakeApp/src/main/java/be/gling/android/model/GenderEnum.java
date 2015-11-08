package be.gling.android.model;

/**
 * Created by florian on 4/06/15.
 */
public enum GenderEnum {
    MALE("male"),
    FEMALE("female");

    private final String text;

    GenderEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static GenderEnum getByText(String gender) {
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getText().equals(gender)) {
                return genderEnum;
            }
        }
        return null;
    }
}
