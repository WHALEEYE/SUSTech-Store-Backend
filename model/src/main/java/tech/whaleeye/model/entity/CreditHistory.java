package tech.whaleeye.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

@Getter
@Setter
public class CreditHistory implements Serializable {
    
    private Integer userId;
    
    private Boolean isAdd;
    
    private BigDecimal creditChange;
    
    private BigDecimal afterCreditScore;
    
    private Integer eventId;
    
    private Date changeTime;

}