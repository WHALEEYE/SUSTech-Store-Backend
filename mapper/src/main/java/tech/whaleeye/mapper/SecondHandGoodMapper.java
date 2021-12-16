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
                                     @Param("typeId") Integer typeId,
                                     @Param("searchKeyword") String searchKeyword);

    Integer countAllGoods(@Param("sold") Boolean sold,
                          @Param("typeId") Integer typeId,
                          @Param("searchKeyword") String searchKeyword);

    List<SecondHandGood> getGoodsByPublisher(@Param("publisher") Integer publisher,
                                             @Param("pageSize") Integer pageSize,
                                             @Param("offset") Integer offset,
                                             @Param("sold") Boolean sold,
                                             @Param("searchKeyword") String searchKeyword);

    Integer countGoodsByPublisher(@Param("publisher") Integer publisher,
                                  @Param("sold") Boolean sold,
                                  @Param("searchKeyword") String searchKeyword);

    Integer insertSecondHandGood(SecondHandGood secondHandGood);

    Integer updateGoodInfo(SecondHandGood secondHandGood);

    Integer setGoodSold(@Param("goodId") Integer goodId);

}
