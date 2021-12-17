package tech.whaleeye.service.Impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.GoodPictureMapper;
import tech.whaleeye.mapper.GoodTypeMapper;
import tech.whaleeye.mapper.SecondHandGoodMapper;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.model.entity.GoodType;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.vo.BriefGoodVO;
import tech.whaleeye.model.vo.FullGoodVO;
import tech.whaleeye.model.vo.GoodPictureVO;
import tech.whaleeye.model.vo.GoodTypeVO;
import tech.whaleeye.service.SecondHandGoodService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecondHandGoodServiceImpl implements SecondHandGoodService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SecondHandGoodMapper secondHandGoodMapper;

    @Autowired
    private GoodPictureMapper goodPictureMapper;

    @Autowired
    private GoodTypeMapper goodTypeMapper;

    @Override
    public FullGoodVO getGoodById(Integer goodId) {
        FullGoodVO fullGoodVO = modelMapper.map(secondHandGoodMapper.getGoodById(goodId), FullGoodVO.class);
        fullGoodVO.setGoodTypeVO(modelMapper.map(goodTypeMapper.getGoodTypeById(fullGoodVO.getId()), GoodTypeVO.class));
        fullGoodVO.setPictureList(modelMapper.map(goodPictureMapper.getPicturesByGoodId(fullGoodVO.getId()), new TypeToken<List<GoodPictureVO>>() {
        }.getType()));
        return fullGoodVO;
    }

    @Override
    public List<BriefGoodVO> getAllGoods(Integer pageSize, Integer pageNo, Integer typeId, String searchKeyword) {
        List<BriefGoodVO> briefGoodVOList = new ArrayList<>();
        BriefGoodVO briefGoodVO;
        for (SecondHandGood secondHandGood : secondHandGoodMapper.getAllGoods(pageSize, (pageNo - 1) * pageSize, typeId, searchKeyword)) {
            briefGoodVO = modelMapper.map(secondHandGood, BriefGoodVO.class);
            GoodType goodType = null;
            if (secondHandGood.getTypeId() != null)
                goodType = goodTypeMapper.getGoodTypeById(secondHandGood.getTypeId());
            if (goodType != null)
                briefGoodVO.setGoodTypeVO(modelMapper.map(goodType, GoodTypeVO.class));
            briefGoodVO.setMainPicPath(goodPictureMapper.getMainPicPathByGoodId(briefGoodVO.getId()));
            briefGoodVOList.add(briefGoodVO);
        }
        return briefGoodVOList;
    }

    @Override
    public List<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo, Boolean sold, String searchKeyword) {
        List<BriefGoodVO> briefGoodVOList = new ArrayList<>();
        BriefGoodVO briefGoodVO;
        for (SecondHandGood secondHandGood : secondHandGoodMapper.getGoodsByPublisher(publisher, pageSize, (pageNo - 1) * pageSize, sold, searchKeyword)) {
            briefGoodVO = modelMapper.map(secondHandGood, BriefGoodVO.class);
            GoodType goodType = null;
            if (secondHandGood.getTypeId() != null)
                goodType = goodTypeMapper.getGoodTypeById(secondHandGood.getTypeId());
            if (goodType != null)
                briefGoodVO.setGoodTypeVO(modelMapper.map(goodType, GoodTypeVO.class));
            briefGoodVO.setMainPicPath(goodPictureMapper.getMainPicPathByGoodId(briefGoodVO.getId()));
            briefGoodVOList.add(briefGoodVO);
        }
        return briefGoodVOList;
    }

    @Override
    public GoodTypeVO getGoodTypeById(Integer typeId) {
        GoodType goodType = goodTypeMapper.getGoodTypeById(typeId);
        return goodType == null ? null : modelMapper.map(goodType, GoodTypeVO.class);
    }

    @Override
    public List<GoodTypeVO> getAllGoodTypes() {
        return modelMapper.map(goodTypeMapper.getAllGoodTypes(), new TypeToken<List<GoodTypeVO>>() {
        }.getType());
    }

    @Override
    public Integer countGoodsByPublisher(Integer publisher, Boolean sold, String searchKeyword) {
        return secondHandGoodMapper.countGoodsByPublisher(publisher, sold, searchKeyword);
    }

    @Override
    public Integer countAllGoods(Integer typeId, String searchKeyword) {
        return secondHandGoodMapper.countAllGoods(typeId, searchKeyword);
    }

    @Override
    public Integer insertSecondHandGood(SecondHandGood secondHandGood) {
        if (secondHandGood.getPrice().doubleValue() < 0f || secondHandGood.getPictureList().isEmpty()) {
            throw new InvalidValueException();
        }
        if (secondHandGoodMapper.insertSecondHandGood(secondHandGood) <= 0) {
            return -1;
        }
        return goodPictureMapper.insertGoodPictures(secondHandGood.getId(), secondHandGood.getPictureList());
    }

    @Override
    public Integer updateGoodInfo(SecondHandGood secondHandGood) {
        if (secondHandGood.getPrice().doubleValue() < 0f) {
            throw new InvalidValueException();
        }
        if (secondHandGoodMapper.updateGoodInfo(secondHandGood) <= 0) {
            return -1;
        }
        goodPictureMapper.deletePicturesByGoodId(secondHandGood.getId());
        return goodPictureMapper.insertGoodPictures(secondHandGood.getId(), secondHandGood.getPictureList());
    }
}
