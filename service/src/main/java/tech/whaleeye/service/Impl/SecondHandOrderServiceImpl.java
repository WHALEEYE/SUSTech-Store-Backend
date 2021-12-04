package tech.whaleeye.service.Impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.SecondHandGoodMapper;
import tech.whaleeye.mapper.SecondHandOrderMapper;
import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.misc.constants.OrderStatus;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.misc.exceptions.BadOrderStatusException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.model.vo.OrderVO;
import tech.whaleeye.service.SecondHandOrderService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SecondHandOrderServiceImpl implements SecondHandOrderService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecondHandOrderMapper secondHandOrderMapper;

    @Autowired
    SecondHandGoodMapper secondHandGoodMapper;

    @Override
    public OrderVO getOrderById(Integer userId, Integer orderId) {
        Boolean userType = secondHandOrderMapper.getUserType(userId, orderId);
        if (userType == null) {
            throw new BadIdentityException();
        }
        OrderVO order = modelMapper.map(secondHandOrderMapper.getOrderById(orderId), OrderVO.class);
        order.setUserType(userType);
        return order;
    }

    @Override
    public ListPage<OrderVO> getOrderByUserId(Integer userId, Boolean userType, Integer orderStatus, Integer pageSize, Integer pageNo) {
        List<OrderVO> orderList = modelMapper.map(secondHandOrderMapper.getOrderByUserId(userId, userType, orderStatus, pageSize, pageSize * (pageNo - 1)), new TypeToken<List<OrderVO>>() {
        }.getType());
        Integer total = secondHandOrderMapper.countOrderByUserId(userId, userType, orderStatus);
        return new ListPage<>(orderList, pageSize, pageNo, total);
    }

    @Override
    public ListPage<OrderVO> getOrderByGoodId(Integer publisher, Integer goodId, Integer pageSize, Integer pageNo) {
        List<OrderVO> orderList = modelMapper.map(secondHandOrderMapper.getOrderByGoodId(publisher, goodId, pageSize, pageSize * (pageNo - 1)), new TypeToken<List<OrderVO>>() {
        }.getType());
        Integer total = secondHandOrderMapper.countOrderByGoodId(publisher, goodId);
        return new ListPage<>(orderList, pageSize, pageNo, total);
    }

    @Override
    public Integer insertSecondHandOrder(SecondHandOrder secondHandOrder) {
        secondHandOrder.setActualPrice(secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId()).getPrice());
        // TODO: Baidu Map Verify Location
        return secondHandOrderMapper.insertSecondHandOrder(secondHandOrder);
    }

    @Override
    public Integer sellerAcknowledge(Integer userId, Integer orderId, Boolean ack, BigDecimal actualPrice) {
        SecondHandOrder secondHandOrder = secondHandOrderMapper.getOrderById(orderId);
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
        if (!userId.equals(secondHandGood.getPublisher())) {
            throw new BadIdentityException();
        } else if (secondHandOrder.getOrderStatus() != OrderStatus.ACK_PENDING.ordinal()) {
            throw new BadOrderStatusException();
        } else if (actualPrice != null && actualPrice.doubleValue() < 0){
            throw new InvalidValueException();
        }
        if (!ack) {
            return secondHandOrderMapper.cancelOrder(orderId);
        } else {
            return secondHandOrderMapper.updateActualPrice(orderId, actualPrice);
        }
    }

    @Override
    public Integer buyerAcknowledge(Integer userId, Integer orderId, Boolean ack) {
        // TODO: Generate two codes and password
        return null;
    }

}
