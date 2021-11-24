package tech.whaleeye.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.SecondHandGoodMapper;
import tech.whaleeye.service.SecondHandOrderService;

@Service
public class SecondHandOrderServiceImpl implements SecondHandOrderService {

    @Autowired
    SecondHandGoodMapper secondHandGoodMapper;

}
