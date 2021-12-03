package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.SecondHandOrder;

import java.util.List;

@Mapper
public interface SecondHandOrderMapper {

    Integer getIdentity(@Param("userId") Integer userId, @Param("orderId") Integer orderId);

    SecondHandOrder getOrderById(@Param("orderId") Integer orderId);

    List<SecondHandOrder> getOrderByUserId(@Param("userId") Integer userId,
                                           @Param("orderStatus") Integer orderStatus,
                                           @Param("isBuyer") Boolean isBuyer,
                                           @Param("pageSize") Integer pageSize,
                                           @Param("offset") Integer offset);

    List<SecondHandOrder> getOrderByGoodId(@Param("publisher") Integer publisher,
                                         @Param("goodId") Integer goodId,
                                         @Param("pageSize") Integer pageSize,
                                         @Param("offset") Integer offset);

}