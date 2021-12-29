package tech.whaleeye.model.vo.SecondHandOrder;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class OrderVO implements Serializable {

    private Integer id;

    private Integer goodId;

    private Integer buyerId;
    /**
     * 0: Waiting for acknowledge; 1: Waiting for payment; 2: Trading; 3: Trade Success; 4: Refund Success; 5: Closed by Seller; 6: Closed by Buyer
     */
    private Integer orderStatus;

    private BigDecimal actualPrice;

    private String tradeLocation;

    private String tradeLatitude;

    private String tradeLongitude;

    private Date tradeTime;

    private String tradePassword;

    private Integer gradeBySeller;

    private Integer gradeByBuyer;

    private String commentBySeller;

    private String commentByBuyer;

    private Date createdTime;

    private Date updatedTime;
    /**
     * false: Seller; true: Buyer
     */
    private Boolean userType;

}
