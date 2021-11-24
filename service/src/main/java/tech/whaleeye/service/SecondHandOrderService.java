package tech.whaleeye.service;

import tech.whaleeye.model.entity.SecondHandOrder;

public interface SecondHandOrderService {

    Integer checkIdentity(Integer userId, Integer orderId);

    SecondHandOrder getOrderById(Integer orderId);

}
