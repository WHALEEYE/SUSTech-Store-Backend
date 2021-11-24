package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.SecondHandOrder;

@Mapper
public interface SecondHandOrderMapper {

    Integer checkIdentity(@Param("userId") Integer userId, @Param("orderId") Integer orderId);

    SecondHandOrder getOrderById(@Param("orderId") Integer orderId);

}