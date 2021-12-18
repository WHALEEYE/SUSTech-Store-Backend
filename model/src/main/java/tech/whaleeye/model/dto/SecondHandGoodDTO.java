package tech.whaleeye.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class SecondHandGoodDTO implements Serializable {

    private Integer id;

    private Integer typeId;

    private String title;

    private String description;

    private BigDecimal price;

    private List<String> picturePathList;

}
