package tech.whaleeye.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GoodPictureVO implements Serializable {

    private String picturePath;

    private Integer sortNo;

}
