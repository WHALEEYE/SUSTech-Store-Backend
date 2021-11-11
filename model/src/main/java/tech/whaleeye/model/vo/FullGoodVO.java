package tech.whaleeye.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FullGoodVO {

    private Integer id;

    private GoodTypeVO goodTypeVO;

    private String title;

    private String description;

    private BigDecimal price;

    private Integer publisher;

    private Boolean sold;

    private List<GoodPictureVO> pictureList;

    private Date createdTime;

    private Date updatedTime;

}
