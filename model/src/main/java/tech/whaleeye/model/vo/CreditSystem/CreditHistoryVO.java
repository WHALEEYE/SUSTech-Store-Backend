package tech.whaleeye.model.vo.CreditSystem;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreditHistoryVO implements Serializable {

    private Boolean isAdd;

    private BigDecimal creditChange;

    private BigDecimal afterCreditScore;

    private String eventName;

    private Date changeTime;

}
