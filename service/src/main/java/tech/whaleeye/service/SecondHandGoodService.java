package tech.whaleeye.service;

import tech.whaleeye.model.entity.SecondHandGood;

import java.math.BigDecimal;
import java.util.List;

public interface SecondHandGoodService {

    List<SecondHandGood> getAllGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo);

    List<SecondHandGood> getUnsoldGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo);

    Integer countAllGoodsByPublisher(Integer publisher);

    Integer countUnsoldGoodsByPublisher(Integer publisher);

    Integer insertSecondHandGood(Integer typeId, String title, String description, BigDecimal price, Integer publisher);

    Integer updateGoodInfo(Integer typeId, String title, String description, Integer publisher, Integer goodId);

}
