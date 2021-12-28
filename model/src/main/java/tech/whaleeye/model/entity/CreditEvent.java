package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreditEvent implements Serializable {

    private Integer id;

    private String eventName;

    private String description;

    private Boolean isAdd;

    private BigDecimal creditChange;

    private Date createdTime;

    private Date updatedTime;

}