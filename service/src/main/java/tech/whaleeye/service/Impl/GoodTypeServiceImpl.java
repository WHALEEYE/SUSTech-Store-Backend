package tech.whaleeye.service.Impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.GoodTypeMapper;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.entity.GoodType;
import tech.whaleeye.model.vo.GoodType.GoodTypeVO;
import tech.whaleeye.service.GoodTypeService;

import java.util.List;

@Service
public class GoodTypeServiceImpl implements GoodTypeService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    GoodTypeMapper goodTypeMapper;

    @Override
    public PageList<GoodTypeVO> listAllGoodTypes(Integer pageSize, Integer pageNo) {
        List<GoodType> goodTypeList = goodTypeMapper.listAllGoodTypesForBack(pageSize, (pageNo - 1) * pageSize);
        List<GoodTypeVO> goodTypeVOList = modelMapper.map(goodTypeList, new TypeToken<List<GoodTypeVO>>() {
        }.getType());
        int total = goodTypeMapper.countAllGoodTypesForBack();
        return new PageList<>(goodTypeVOList, pageSize, pageNo, total);
    }

    @Override
    public Boolean createNewType(String typeName) {
        return goodTypeMapper.createNewType(typeName) > 0;
    }

    @Override
    public Boolean updateTypeName(Integer typeId, String typeName) {
        return goodTypeMapper.updateTypeName(typeId, typeName) > 0;
    }

    @Override
    public Boolean moveUp(Integer typeId) {
        return goodTypeMapper.moveUp(typeId);
    }

    @Override
    public Boolean moveDown(Integer typeId) {
        return goodTypeMapper.moveDown(typeId);
    }

    @Override
    public Boolean deleteType(Integer typeId) {
        return goodTypeMapper.deleteType(typeId) > 0;
    }

}
