package tech.whaleeye.service;

import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.model.vo.OrderVO;

import java.math.BigDecimal;

public interface SecondHandOrderService {

    OrderVO getOrderById(Integer userId, Integer orderId);

    ListPage<OrderVO> getOrderByUserId(Integer userId, Boolean userType, Integer orderStatus, Integer pageSize, Integer pageNo);

    ListPage<OrderVO> getOrderByGoodId(Integer publisher, Integer goodId, Integer pageSize, Integer pageNo);

    Integer insertSecondHandOrder(SecondHandOrder secondHandOrder);

    Integer sellerAcknowledge(Integer userId, Integer orderId, Boolean ack, BigDecimal actualPrice);

    Integer buyerAcknowledge(Integer userId, Integer orderId, Boolean ack);

}
