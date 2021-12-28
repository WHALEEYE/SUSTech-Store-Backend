package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InputHistoryMapper {

    Integer countWrongInputTime(@Param("userId") Integer userId, @Param("orderId") Integer orderId);

    void insertInputHistory(@Param("userId") Integer userId,
                            @Param("orderId") Integer orderId,
                            @Param("inputCode") String inputCode,
                            @Param("result") Boolean result);

}