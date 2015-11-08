package be.gling.android.model.dto;

import be.gling.android.model.dto.technical.DTO;

/**
 * Created by florian on 6/01/15.
 */
public class LangDTO extends DTO {

    private String language;

    private String code;

    public LangDTO() {
    }

    public LangDTO(String language, String code) {
        this.language = language;
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    @Override
    public String toString() {
        return "LangDTO{" +
                "language='" + language + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
