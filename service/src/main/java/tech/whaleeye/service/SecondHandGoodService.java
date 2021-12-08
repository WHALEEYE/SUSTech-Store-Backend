package tech.whaleeye.service;

import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.vo.BriefGoodVO;
import tech.whaleeye.model.vo.FullGoodVO;
import tech.whaleeye.model.vo.GoodTypeVO;

import java.util.List;

public interface SecondHandGoodService {

    FullGoodVO getGoodById(Integer goodId);

    List<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer status, Integer pageSize, Integer pageNo);

    GoodTypeVO getGoodTypeById(Integer typeId);

    List<GoodTypeVO> getAllGoodTypes();

    Integer countAllGoodsByPublisher(Integer publisher);

    Integer countUnsoldGoodsByPublisher(Integer publisher);

    Integer insertSecondHandGood(SecondHandGood secondHandGood);

    Integer updateGoodInfo(SecondHandGood secondHandGood);

}
