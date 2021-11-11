package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class GoodPicture implements Serializable {

    private Integer id;

    private Integer goodId;

    private String goodPath;

    private Integer sortNo;

    private Date createdTime;

}
