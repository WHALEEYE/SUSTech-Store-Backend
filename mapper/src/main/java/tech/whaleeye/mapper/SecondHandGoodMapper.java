package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.entity.SecondHandGood;

import java.util.List;

@Mapper
public interface SecondHandGoodMapper {

    SecondHandGood getGoodById(@Param("goodId") Integer goodId);

    List<SecondHandGood> listAllGoods(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset, @Param("typeId") Integer typeId, @Param("searchKeyword") String searchKeyword);

    Integer countAllGoods(@Param("typeId") Integer typeId, @Param("searchKeyword") String searchKeyword);

    List<SecondHandGood> getGoodsByPublisher(@Param("publisher") Integer publisher, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset, @Param("sold") Boolean sold, @Param("searchKeyword") String searchKeyword);

    Integer countGoodsByPublisher(@Param("publisher") Integer publisher, @Param("sold") Boolean sold, @Param("searchKeyword") String searchKeyword);

    Boolean deleteSecondHandGood(@Param("goodId") Integer goodId, @Param("userId") Integer userId);

    List<SecondHandGood> listCollectedGoods(@Param("userId") Integer userId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer countCollectedGoods(@Param("userId") Integer userId);

    Boolean isCollecting(@Param("userId") Integer userId, @Param("goodId") Integer goodId);

    Integer insertSecondHandGood(@Param("good") SecondHandGood secondHandGood);

    Integer collectGood(@Param("userId") Integer userId, @Param("goodId") Integer goodId);

    Integer cancelCollectGood(@Param("userId") Integer userId, @Param("goodId") Integer goodId);

    Integer updateGoodInfo(SecondHandGoodDTO secondHandGoodDTO);

    /* used in background system */

    List<SecondHandGood> listAllGoodsForBack(@Param("pageSize") Integer pageSize,
                                             @Param("offset") Integer offset,
                                             @Param("searchNickname") String searchNickname,
                                             @Param("searchPhoneNumber") String searchPhoneNumber,
                                             @Param("searchKeyword") String searchKeyword);

    Integer countAllGoodsForBack(@Param("searchNickname") String searchNickname,
                                 @Param("searchPhoneNumber") String searchPhoneNumber,
                                 @Param("searchKeyword") String searchKeyword);

}
