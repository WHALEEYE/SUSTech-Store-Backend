package tech.whaleeye.service;

import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.vo.BriefGoodVO;
import tech.whaleeye.model.vo.FullGoodVO;
import tech.whaleeye.model.vo.GoodTypeVO;

import java.util.List;

public interface SecondHandGoodService {

    FullGoodVO getGoodById(Integer goodId);

    List<BriefGoodVO> getAllGoods(Integer pageSize, Integer pageNo, Boolean sold, Integer typeId);

    List<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo, Boolean sold);

    GoodTypeVO getGoodTypeById(Integer typeId);

    List<GoodTypeVO> getAllGoodTypes();

    Integer countAllGoods(Boolean sold, Integer typeId);

    Integer countGoodsByPublisher(Integer publisher, Boolean sold);
    
    Integer insertSecondHandGood(SecondHandGood secondHandGood);

    Integer updateGoodInfo(SecondHandGood secondHandGood);

}
