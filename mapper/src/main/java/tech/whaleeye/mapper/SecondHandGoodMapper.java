package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.SecondHandGood;

import java.util.List;

@Mapper
public interface SecondHandGoodMapper {

    SecondHandGood getGoodById(@Param("goodId") Integer goodId);

    List<SecondHandGood> getAllGoods(@Param("pageSize") Integer pageSize,
                                     @Param("offset") Integer offset,
                                     @Param("sold") Boolean sold,
                                     @Param("typeId") Integer typeId);

    Integer countAllGoods(@Param("sold") Boolean sold, @Param("typeId") Integer typeId);

    List<SecondHandGood> getGoodsByPublisher(@Param("publisher") Integer publisher,
                                             @Param("pageSize") Integer pageSize,
                                             @Param("offset") Integer offset,
                                             @Param("sold") Boolean sold);

    Integer countGoodsByPublisher(@Param("publisher") Integer publisher, @Param("sold") Boolean sold);

    Integer insertSecondHandGood(SecondHandGood secondHandGood);

    Integer updateGoodInfo(SecondHandGood secondHandGood);

    Integer setGoodSold(@Param("goodId") Integer goodId);

}
