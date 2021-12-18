package tech.whaleeye.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BackGoodVO implements Serializable {

    private Integer id;

    private String goodType;

    private String title;

    private String description;

    private BigDecimal price;

    private String publisherName;

    private String publisherPhone;

    private Boolean sold;

    private Date createdTime;

    private Date updatedTime;

}
