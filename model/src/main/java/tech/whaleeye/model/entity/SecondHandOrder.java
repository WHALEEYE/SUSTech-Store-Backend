package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class SecondHandOrder implements Serializable {

    private Integer id;

    private Integer goodId;

    private Integer buyerId;
    /**
     * 1: Waiting for acknowledge; 2: Waiting for payment; 3: Trading; 4: Trade Success; 5: Refund Success; 6: Closed
     */
    private Integer orderStatus;

    private BigDecimal actualPrice;

    private String tradeLocation;

    private String tradeLatitude;

    private String tradeLongitude;

    private Date tradeTime;

    private String tradePassword;

    private Integer dealCode;

    private Integer refundCode;

    private Integer gradeBySeller;

    private Integer gradeByBuyer;

    private String commentBySeller;

    private String commentByBuyer;

    private Date createdTime;

    private Date updatedTime;

}