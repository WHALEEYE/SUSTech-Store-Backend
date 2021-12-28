package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import tech.whaleeye.model.entity.SecondHandOrder;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SecondHandOrderMapper {

    Boolean getUserType(@Param("userId") Integer userId, @Param("orderId") Integer orderId);

    SecondHandOrder getOrderById(@Param("orderId") Integer orderId);

    Boolean alreadyHaveOrder(@Param("userId") Integer userId, @Param("goodId") Integer goodId);

    List<SecondHandOrder> getOrderByUserId(@Param("userId") Integer userId,
                                           @Param("userType") Boolean userType,
                                           @Param("orderStatus") Integer orderStatus,
                                           @Param("pageSize") Integer pageSize,
                                           @Param("offset") Integer offset);

    Integer countOrderByUserId(@Param("userId") Integer userId,
                               @Param("userType") Boolean userType,
                               @Param("orderStatus") Integer orderStatus);

    List<SecondHandOrder> getOrderByGoodId(@Param("publisher") Integer publisher,
                                           @Param("goodId") Integer goodId,
                                           @Param("pageSize") Integer pageSize,
                                           @Param("offset") Integer offset);

    Integer countOrderByGoodId(@Param("publisher") Integer publisher,
                               @Param("goodId") Integer goodId);

    Boolean buyerAck(@Param("orderId") Integer orderId,
                     @Param("dealCode") String dealCode,
                     @Param("refundCode") String refundCode,
                     @Param("tradePassword") String tradePassword);

    void orderConfirm(@Param("orderId") Integer orderId);

    void orderRefund(@Param("orderId") Integer orderId);

    Integer insertSecondHandOrder(SecondHandOrder secondHandOrder);

    Integer cancelOrder(@Param("orderId") Integer orderId);

    Integer updateActualPrice(@Param("orderId") Integer orderId, @Param("actualPrice") BigDecimal actualPrice);

    Integer updateCommentAndGrade(@Param("orderId") Integer orderId,
                                  @Param("comment") String comment,
                                  @Param("grade") Integer grade,
                                  @Param("userType") Boolean userType);

    /* used in background system */

    List<SecondHandOrder> listAllOrders(@Param("pageSize") Integer pageSize,
                                        @Param("offset") Integer offset,
                                        @Nullable @Param("orderId") Integer orderId);

    Integer countAllOrders(@Nullable @Param("orderId") Integer orderId);

}