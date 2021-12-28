package tech.whaleeye.model.vo.CreditSystem;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CreditEventVO implements Serializable {

    private Integer id;

    private String eventName;

    private String description;

    private Boolean isAdd;

    private BigDecimal creditChange;

}
