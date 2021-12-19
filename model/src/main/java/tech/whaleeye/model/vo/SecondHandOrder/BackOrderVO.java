package tech.whaleeye.model.vo.SecondHandOrder;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class BackOrderVO implements Serializable {

    private Integer id;

    private String goodTitle;

    private String sellerNickname;

    private String sellerPhoneNumber;

    private String buyerNickname;

    private String buyerPhoneNumber;
    /**
     * 0: Waiting for acknowledge; 1: Waiting for payment; 2: Trading; 3: Trade Success; 4: Refund Success; 5: Closed
     */
    private Integer orderStatus;

    private BigDecimal actualPrice;

    private String tradeLocation;

    private Date tradeTime;

    private String tradePassword;

    private String dealCode;

    private String refundCode;

    private Date createdTime;

    private Date updatedTime;

}
