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
    /**
     * 1: UG Student; 2: Research Student; 3: Faculty
     */
    private Integer career;

    private Integer admissionYear;

    private String alipayAccount;

    private BigDecimal creditScore;

    private BigDecimal totalSoldValue;

    private Boolean secondHandNotification;

    private Boolean agentServiceNotification;

    private Boolean apiTradeNotification;

    private Boolean banned;

    private String vCode;

    private Date vCodeExpireTime;

    private Date createdTime;

    private Date updatedTime;

}