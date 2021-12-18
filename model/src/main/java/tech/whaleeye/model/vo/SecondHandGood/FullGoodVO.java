package tech.whaleeye.model.vo.SecondHandGood;

import lombok.Getter;
import lombok.Setter;
import tech.whaleeye.model.vo.GoodType.GoodTypeVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FullGoodVO implements Serializable {

    private Integer id;

    private GoodTypeVO goodTypeVO;

    private String title;

    private String description;

    private BigDecimal price;

    private Integer publisher;

    private Boolean sold;

    private List<String> picturePathList;

    private Date createdTime;

    private Date updatedTime;

}
