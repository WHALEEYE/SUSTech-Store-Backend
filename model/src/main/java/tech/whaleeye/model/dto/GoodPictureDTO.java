package tech.whaleeye.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GoodPictureDTO implements Serializable {

    private String picturePath;

    private Integer sortNo;

}
