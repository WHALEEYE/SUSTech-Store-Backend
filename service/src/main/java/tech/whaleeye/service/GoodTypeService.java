package tech.whaleeye.service;

import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.vo.GoodType.GoodTypeVO;

public interface GoodTypeService {

    PageList<GoodTypeVO> listAllGoodTypes(Integer pageSize, Integer pageNo);

    Boolean createNewType(String typeName);

    Boolean updateTypeName(Integer typeId, String typeName);

    Boolean moveUp(Integer typeId);

    Boolean moveDown(Integer typeId);

    Boolean deleteType(Integer typeId);

}
