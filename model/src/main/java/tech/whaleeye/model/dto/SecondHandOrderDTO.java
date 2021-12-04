package tech.whaleeye.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class SecondHandOrderDTO implements Serializable {

    private Integer goodId;

    private String tradeLocation;

    private String tradeLatitude;

    private String tradeLongitude;

    private Date tradeTime;

}
