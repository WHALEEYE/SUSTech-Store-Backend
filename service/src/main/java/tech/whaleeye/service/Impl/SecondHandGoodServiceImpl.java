package tech.whaleeye.service.Impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.GoodPictureMapper;
import tech.whaleeye.mapper.GoodTypeMapper;
import tech.whaleeye.mapper.SecondHandGoodMapper;
import tech.whaleeye.mapper.StoreUserMapper;
import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.entity.GoodType;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.*;
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

    @Autowired
    private StoreUserMapper storeUserMapper;

    @Override
    public FullGoodVO getGoodById(Integer goodId) {
        FullGoodVO fullGoodVO = modelMapper.map(secondHandGoodMapper.getGoodById(goodId), FullGoodVO.class);
        fullGoodVO.setGoodTypeVO(modelMapper.map(goodTypeMapper.getGoodTypeById(fullGoodVO.getId()), GoodTypeVO.class));
        fullGoodVO.setPictureList(modelMapper.map(goodPictureMapper.getPicturesByGoodId(fullGoodVO.getId()), new TypeToken<List<GoodPictureVO>>() {
        }.getType()));
        return fullGoodVO;
    }

    @Override
    public ListPage<BriefGoodVO> listAllGoods(Integer pageSize, Integer pageNo, Integer typeId, String searchKeyword) {
        List<BriefGoodVO> briefGoodVOList = new ArrayList<>();
        List<SecondHandGood> secondHandGoodList = secondHandGoodMapper.listAllGoods(pageSize, (pageNo - 1) * pageSize, typeId, searchKeyword);
        BriefGoodVO briefGoodVO;
        for (SecondHandGood secondHandGood : secondHandGoodList) {
            briefGoodVO = modelMapper.map(secondHandGood, BriefGoodVO.class);
            GoodType goodType = null;
            if (secondHandGood.getTypeId() != null)
                goodType = goodTypeMapper.getGoodTypeById(secondHandGood.getTypeId());
            if (goodType != null)
                briefGoodVO.setGoodTypeVO(modelMapper.map(goodType, GoodTypeVO.class));
            briefGoodVO.setMainPicPath(goodPictureMapper.getMainPicPathByGoodId(briefGoodVO.getId()));
            briefGoodVOList.add(briefGoodVO);
        }
        int total = secondHandGoodMapper.countAllGoods(typeId, searchKeyword);
        return new ListPage<>(briefGoodVOList, pageSize, pageNo, total);
    }

    @Override
    public ListPage<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo, Boolean sold, String searchKeyword) {
        List<BriefGoodVO> briefGoodVOList = new ArrayList<>();
        List<SecondHandGood> secondHandGoodList = secondHandGoodMapper.getGoodsByPublisher(publisher, pageSize, (pageNo - 1) * pageSize, sold, searchKeyword);
        BriefGoodVO briefGoodVO;
        GoodType goodType;
        for (SecondHandGood secondHandGood : secondHandGoodList) {
            briefGoodVO = modelMapper.map(secondHandGood, BriefGoodVO.class);
            goodType = null;
            if (secondHandGood.getTypeId() != null)
                goodType = goodTypeMapper.getGoodTypeById(secondHandGood.getTypeId());
            if (goodType != null)
                briefGoodVO.setGoodTypeVO(modelMapper.map(goodType, GoodTypeVO.class));
            briefGoodVO.setMainPicPath(goodPictureMapper.getMainPicPathByGoodId(briefGoodVO.getId()));
            briefGoodVOList.add(briefGoodVO);
        }
        int total = secondHandGoodMapper.countGoodsByPublisher(publisher, sold, searchKeyword);
        return new ListPage<>(briefGoodVOList, pageSize, pageNo, total);
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
    public Boolean insertSecondHandGood(SecondHandGoodDTO secondHandGoodDTO) {
        if (secondHandGoodDTO.getPrice().doubleValue() < 0f || secondHandGoodDTO.getPictureList().isEmpty()) {
            throw new InvalidValueException();
        }
        if (secondHandGoodMapper.insertSecondHandGood(MiscUtils.currentUserId(), secondHandGoodDTO) <= 0) {
            return false;
        }
        return goodPictureMapper.insertGoodPictures(secondHandGoodDTO.getId(), secondHandGoodDTO.getPictureList()) > 0;
    }

    @Override
    public Boolean updateGoodInfo(SecondHandGoodDTO secondHandGoodDTO) {
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandGoodDTO.getId());
        if (!secondHandGood.getId().equals(MiscUtils.currentUserId())) {
            throw new BadIdentityException();
        } else if (secondHandGoodDTO.getPrice().doubleValue() < 0f) {
            throw new InvalidValueException();
        }
        if (secondHandGoodMapper.updateGoodInfo(secondHandGoodDTO) <= 0) {
            return false;
        }
        goodPictureMapper.deletePicturesByGoodId(secondHandGoodDTO.getId());
        return goodPictureMapper.insertGoodPictures(secondHandGoodDTO.getId(), secondHandGoodDTO.getPictureList()) > 0;
    }

    public Boolean deleteGood(Integer goodId, Integer userId) {
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(goodId);
        if (!secondHandGood.getPublisher().equals(userId)) {
            throw new BadIdentityException();
        }
        return secondHandGoodMapper.deleteSecondHandGood(goodId, userId);
    }

    @Override
    public ListPage<BackGoodVO> listAllGoodsForBack(Integer pageSize, Integer pageNo, String searchNickname, String searchPhoneNumber, String searchKeyword) {
        List<BackGoodVO> backGoodVOList = new ArrayList<>();
        List<SecondHandGood> secondHandGoodList = secondHandGoodMapper.listAllGoodsForBack(pageSize, (pageNo - 1) * pageSize, searchNickname, searchPhoneNumber, searchKeyword);
        BackGoodVO backGoodVO;
        StoreUser storeUser;
        GoodType goodType;
        for (SecondHandGood secondHandGood : secondHandGoodList) {
            backGoodVO = modelMapper.map(secondHandGood, BackGoodVO.class);
            storeUser = storeUserMapper.getUserById(secondHandGood.getPublisher());
            backGoodVO.setPublisherName(storeUser.getNickname());
            backGoodVO.setPublisherPhone(storeUser.getPhoneNumber());
            goodType = null;
            if (secondHandGood.getTypeId() != null)
                goodType = goodTypeMapper.getGoodTypeById(secondHandGood.getTypeId());
            backGoodVO.setGoodType(goodType == null ? "No Type" : goodType.getTypeName());
        }
        int total = secondHandGoodMapper.countAllGoodsForBack(searchNickname, searchPhoneNumber, searchKeyword);
        return new ListPage<>(backGoodVOList, pageSize, pageNo, total);
    }

    @Override
    public Boolean deleteGoodForBack(Integer goodId, Integer userId) {
        return secondHandGoodMapper.deleteSecondHandGood(goodId, userId);
    }
}
