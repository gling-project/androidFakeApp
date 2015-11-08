package be.gling.android.model.dto;

import be.gling.android.model.dto.technical.DTO;

/**
 * Created by florian on 23/05/15.
 */
public class StoredFileDTO extends DTO implements Comparable<StoredFileDTO> {

    private Long id;

    private Boolean isImage;

    private String originalName;

    private String storedName;

    private String storedNameOriginalSize;

    private String comment;

    private Integer fileOrder = 0;
    private String storedNameTemp;

    public Integer getFileOrder() {
        return fileOrder;
    }

    public void setFileOrder(Integer fileOrder) {
        this.fileOrder = fileOrder;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(Boolean isImage) {
        this.isImage = isImage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public String getStoredNameOriginalSize() {
        return storedNameOriginalSize;
    }

    public void setStoredNameOriginalSize(String storedNameOriginalSize) {
        this.storedNameOriginalSize = storedNameOriginalSize;
    }

    @Override
    public String toString() {
        return "StoredFileDTO{" +
                "id=" + id +
                ", isImage=" + isImage +
                ", originalName='" + originalName + '\'' +
                '}';
    }

    @Override
    public int compareTo(StoredFileDTO o) {
        return this.getFileOrder().compareTo(o.getFileOrder());
    }

    public void setStoredNameTemp(String storedNameTemp) {
        this.storedNameTemp = storedNameTemp;
    }

    public String getStoredNameTemp() {
        return storedNameTemp;
    }
}
