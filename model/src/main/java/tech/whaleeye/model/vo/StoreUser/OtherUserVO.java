package tech.whaleeye.model.vo.StoreUser;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class OtherUserVO implements Serializable {

    private Integer id;

    private String phoneNumber;

    private String nickname;

    private String cardNumber;

    private String introduction;

    private Integer followerCount;

    private Integer followingCount;

    private String avatarPath;
    /**
     * true: male; false: female
     */
    private Boolean sex;

    private BigDecimal creditScore;

    private BigDecimal totalSoldValue;

    private Boolean banned;

    private Date createdTime;

}
