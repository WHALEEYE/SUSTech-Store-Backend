package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class GoodType implements Serializable {

    private Integer id;

    private String typeName;

    private Integer sortNo;

    private Date createdTime;

}
