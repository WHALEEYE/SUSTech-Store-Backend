package tech.whaleeye.service;

import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.model.vo.OrderVO;

import java.util.List;

public interface SecondHandOrderService {

    OrderVO getOrderById(Integer userId, Integer orderId);

    List<SecondHandOrder> getOrderByBuyer(Integer buyerId, Integer orderStatus);

    List<SecondHandOrder> getOrderBySeller(Integer publisher, Integer orderStatus);

    List<SecondHandOrder> getOrderByGood(Integer publisher, Integer goodId);

}
