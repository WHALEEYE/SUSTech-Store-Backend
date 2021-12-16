package tech.whaleeye.service;

import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.vo.BriefGoodVO;
import tech.whaleeye.model.vo.FullGoodVO;
import tech.whaleeye.model.vo.GoodTypeVO;

import java.util.List;

public interface SecondHandGoodService {

    FullGoodVO getGoodById(Integer goodId);

    List<BriefGoodVO> getAllGoods(Integer pageSize, Integer pageNo, Integer typeId, String searchKeyword);

    List<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo, Boolean sold, String searchKeyword);

    GoodTypeVO getGoodTypeById(Integer typeId);

    List<GoodTypeVO> getAllGoodTypes();

    Integer countAllGoods(Integer typeId, String searchKeyword);

    Integer countGoodsByPublisher(Integer publisher, Boolean sold, String searchKeyword);
    
    Integer insertSecondHandGood(SecondHandGood secondHandGood);

    Integer updateGoodInfo(SecondHandGood secondHandGood);

}
