package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SecondHandGood implements Serializable {

    private Integer id;

    private Integer typeId;

    private String title;

    private String description;

    private BigDecimal price;

    private Integer publisher;

    private Boolean sold;

    private Date createdTime;

    private Date updatedTime;

}