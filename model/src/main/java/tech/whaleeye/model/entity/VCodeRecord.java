package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class VCodeRecord implements Serializable {

    private Integer id;

    private Integer storeUserId;

    private String phoneNumber;

    private String cardNumber;

    private String vCode;

    private Date expireTime;
    /**
     * 0: Login; 1: Change Password; 2: Change Alipay; 3: Cancel Account; 4: Mail Verification
     */
    private Integer type;

    private Date createdTime;

    private Date usedTime;
}