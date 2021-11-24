package tech.whaleeye.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.SecondHandOrderMapper;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.service.SecondHandOrderService;

@Service
public class SecondHandOrderServiceImpl implements SecondHandOrderService {

    @Autowired
    SecondHandOrderMapper secondHandOrderMapper;

    @Override
    public Integer checkIdentity(Integer userId, Integer orderId) {
        Integer userType = secondHandOrderMapper.checkIdentity(userId, orderId);
        if (userType == 0) {
            throw new BadIdentityException();
        }
        return userType;
    }

    @Override
    public SecondHandOrder getOrderById(Integer orderId) {
        return secondHandOrderMapper.getOrderById(orderId);
    }
}
