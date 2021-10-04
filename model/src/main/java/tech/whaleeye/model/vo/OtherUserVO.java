package tech.whaleeye.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class OtherUserVO implements Serializable {

    private Integer id;

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

    private BigDecimal creditScore;

    private BigDecimal totalSoldValue;

    private Boolean banned;

    private Date createdTime;

}
