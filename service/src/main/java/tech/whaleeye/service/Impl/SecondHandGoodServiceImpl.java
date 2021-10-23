package tech.whaleeye.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.SecondHandGoodMapper;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.service.SecondHandGoodService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SecondHandGoodServiceImpl implements SecondHandGoodService {

    @Autowired
    SecondHandGoodMapper secondHandGoodMapper;

    @Override
    public List<SecondHandGood> getAllGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo) {
        return secondHandGoodMapper.getGoodsByPublisher(publisher, false, pageSize, (pageNo - 1) * pageSize);
    }

    @Override
    public List<SecondHandGood> getUnsoldGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo) {
        return secondHandGoodMapper.getGoodsByPublisher(publisher, true, pageSize, (pageNo - 1) * pageSize);
    }

    @Override
    public Integer countAllGoodsByPublisher(Integer publisher) {
        return secondHandGoodMapper.countGoodsByPublisher(publisher, false);
    }

    @Override
    public Integer countUnsoldGoodsByPublisher(Integer publisher) {
        return secondHandGoodMapper.countGoodsByPublisher(publisher, true);
    }

    @Override
    public Integer insertSecondHandGood(Integer typeId, String title, String description, BigDecimal price, Integer publisher) {
        return secondHandGoodMapper.insertSecondHandGood(typeId, title, description, price, publisher);
    }

    @Override
    public Integer updateGoodInfo(Integer typeId, String title, String description, Integer publisher, Integer goodId) {
        return secondHandGoodMapper.updateGoodInfo(typeId, title, description, publisher, goodId);
    }
}
