package tech.whaleeye.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class GoodTypeVO implements Serializable {

    private Integer id;

    private String typeName;

}
