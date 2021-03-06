package tech.whaleeye.service;

import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.vo.GoodType.GoodTypeVO;
import tech.whaleeye.model.vo.SecondHandGood.BackGoodVO;
import tech.whaleeye.model.vo.SecondHandGood.BriefGoodVO;
import tech.whaleeye.model.vo.SecondHandGood.FullGoodVO;

import java.util.List;

public interface SecondHandGoodService {

    FullGoodVO getGoodById(Integer goodId);

    BriefGoodVO getBriefGoodById(Integer goodId);

    PageList<BriefGoodVO> listAllGoods(Integer pageSize, Integer pageNo, Integer typeId, String searchKeyword);

    PageList<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo, Boolean sold, String searchKeyword);

    PageList<BriefGoodVO> listCollectedGoods(Integer userId, Integer pageSize, Integer pageNo);

    GoodTypeVO getGoodTypeById(Integer typeId);

    List<GoodTypeVO> getAllGoodTypes();

    Boolean insertSecondHandGood(SecondHandGoodDTO secondHandGoodDTO);

    Boolean collectGood(Integer goodId);

    Boolean cancelCollectGood(Integer goodId);

    Boolean updateGoodInfo(SecondHandGoodDTO secondHandGoodDTO);

    Boolean deleteGood(Integer goodId, Integer userId);

    /* used in background system */

    PageList<BackGoodVO> listAllGoodsForBack(Integer pageSize, Integer pageNo, String searchNickname, String searchPhoneNumber, String searchKeyword);

    Boolean deleteGoodForBack(Integer goodId, Integer userId);
}
