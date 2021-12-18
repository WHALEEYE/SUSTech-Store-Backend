package tech.whaleeye.service;

import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.vo.BackGoodVO;
import tech.whaleeye.model.vo.BriefGoodVO;
import tech.whaleeye.model.vo.FullGoodVO;
import tech.whaleeye.model.vo.GoodTypeVO;

import java.util.List;

public interface SecondHandGoodService {

    FullGoodVO getGoodById(Integer goodId);

    ListPage<BriefGoodVO> listAllGoods(Integer pageSize, Integer pageNo, Integer typeId, String searchKeyword);

    ListPage<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo, Boolean sold, String searchKeyword);

    GoodTypeVO getGoodTypeById(Integer typeId);

    List<GoodTypeVO> getAllGoodTypes();

    Boolean insertSecondHandGood(SecondHandGoodDTO secondHandGoodDTO);

    Boolean updateGoodInfo(SecondHandGoodDTO secondHandGoodDTO);

    Boolean deleteGood(Integer goodId, Integer userId);

    /* used in background system */

    ListPage<BackGoodVO> listAllGoodsForBack(Integer pageSize, Integer pageNo, String searchNickname, String searchPhoneNumber, String searchKeyword);

    Boolean deleteGoodForBack(Integer goodId, Integer userId);
}
