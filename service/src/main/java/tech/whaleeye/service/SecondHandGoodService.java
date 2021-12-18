package tech.whaleeye.service;

import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.vo.SecondHandGood.BackGoodVO;
import tech.whaleeye.model.vo.SecondHandGood.BriefGoodVO;
import tech.whaleeye.model.vo.SecondHandGood.FullGoodVO;
import tech.whaleeye.model.vo.GoodType.GoodTypeVO;

import java.util.List;

public interface SecondHandGoodService {

    FullGoodVO getGoodById(Integer goodId);

    PageList<BriefGoodVO> listAllGoods(Integer pageSize, Integer pageNo, Integer typeId, String searchKeyword);

    PageList<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo, Boolean sold, String searchKeyword);

    GoodTypeVO getGoodTypeById(Integer typeId);

    List<GoodTypeVO> getAllGoodTypes();

    Boolean insertSecondHandGood(SecondHandGoodDTO secondHandGoodDTO);

    Boolean updateGoodInfo(SecondHandGoodDTO secondHandGoodDTO);

    Boolean deleteGood(Integer goodId, Integer userId);

    /* used in background system */

    PageList<BackGoodVO> listAllGoodsForBack(Integer pageSize, Integer pageNo, String searchNickname, String searchPhoneNumber, String searchKeyword);

    Boolean deleteGoodForBack(Integer goodId, Integer userId);
}
