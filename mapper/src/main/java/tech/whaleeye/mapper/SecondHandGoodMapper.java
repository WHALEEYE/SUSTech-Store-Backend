package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.entity.SecondHandGood;

import java.util.List;

@Mapper
public interface SecondHandGoodMapper {

    SecondHandGood getGoodById(@Param("goodId") Integer goodId);

    List<SecondHandGood> getGoodsByPublisher(@Param("publisher") Integer publisher,
                                             @Param("notSold") Boolean notSold,
                                             @Param("pageSize") Integer pageSize,
                                             @Param("offset") Integer offset);

    Integer countGoodsByPublisher(@Param("publisher") Integer publisher, @Param("notSold") Boolean notSold);

    Integer insertSecondHandGood(SecondHandGoodDTO secondHandGood);

    Integer updateGoodInfo(SecondHandGoodDTO secondHandGood);

    Integer setGoodSold(@Param("goodId") Integer goodId);

}
