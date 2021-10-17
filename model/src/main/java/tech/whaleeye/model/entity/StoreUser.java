package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class StoreUser implements Serializable {

    private Integer id;

    private String phoneNumber;

    private String nickname;

    private String cardNumber;

    private String password;

    private String salt;

    private String introduction;

    private String avatarPath;
    /**
     * true: male; false: female
     */
    private Boolean sex;

    private String alipayAccount;

    private BigDecimal creditScore;

    private BigDecimal totalSoldValue;

    private Boolean secondHandNotification;

    private Boolean agentServiceNotification;

    private Boolean apiTradeNotification;

    private Boolean banned;

    private Date createdTime;

    private Date updatedTime;

}