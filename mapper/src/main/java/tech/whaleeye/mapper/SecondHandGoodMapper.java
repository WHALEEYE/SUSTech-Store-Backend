package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.SecondHandGood;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SecondHandGoodMapper {

    List<SecondHandGood> getGoodsByPublisher(@Param("publisher") Integer publisher,
                                             @Param("notSold") Boolean notSold,
                                             @Param("pageSize") Integer pageSize,
                                             @Param("offset") Integer offset);

    Integer countGoodsByPublisher(@Param("publisher") Integer publisher, @Param("notSold") Boolean notSold);

    Integer insertSecondHandGood(@Param("typeId") Integer typeId,
                                 @Param("title") String title,
                                 @Param("description") String description,
                                 @Param("price") BigDecimal price,
                                 @Param("publisher") Integer publisher);

    Integer updateGoodInfo(@Param("typeId") Integer typeId,
                           @Param("title") String title,
                           @Param("description") String description,
                           @Param("publisher") Integer publisher,
                           @Param("goodId") Integer goodId);

    Integer setGoodSold(@Param("goodId") Integer goodId);

}
