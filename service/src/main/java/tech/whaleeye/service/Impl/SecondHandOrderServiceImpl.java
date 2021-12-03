package tech.whaleeye.service.Impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.SecondHandOrderMapper;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.model.vo.OrderVO;
import tech.whaleeye.service.SecondHandOrderService;

import java.util.List;

@Service
public class SecondHandOrderServiceImpl implements SecondHandOrderService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecondHandOrderMapper secondHandOrderMapper;

    @Override
    public OrderVO getOrderById(Integer userId, Integer orderId) {
        Integer userType = secondHandOrderMapper.getIdentity(userId, orderId);
        if (userType == -1) {
            throw new BadIdentityException();
        }
        OrderVO order = modelMapper.map(secondHandOrderMapper.getOrderById(orderId), OrderVO.class);
        order.setIdentity(userType);
        return order;
    }

    @Override
    public List<SecondHandOrder> getOrderByBuyer(Integer buyerId, Integer orderStatus) {
        return null;
    }

    @Override
    public List<SecondHandOrder> getOrderBySeller(Integer publisher, Integer orderStatus) {
        return null;
    }

    @Override
    public List<SecondHandOrder> getOrderByGood(Integer publisher, Integer goodId) {
        return null;
    }
}
