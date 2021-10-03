package tech.whaleeye.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class StoreUserVO {

    private String phoneNumber;

    private String nickname;

    private String cardNumber;

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

    private String admissionYear;

    private String alipayAccount;

    private BigDecimal creditScore;

    private BigDecimal totalSoldValue;

    private Boolean secondHandNotification;

    private Boolean agentServiceNotification;

    private Boolean apiTradeNotification;

    private Boolean banned;

    private Date createdTime;
}
