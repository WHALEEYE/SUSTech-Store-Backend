package tech.whaleeye.service.Impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.GoodPictureMapper;
import tech.whaleeye.mapper.GoodTypeMapper;
import tech.whaleeye.mapper.SecondHandGoodMapper;
import tech.whaleeye.mapper.StoreUserMapper;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.exceptions.LowCreditException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.entity.GoodType;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.GoodType.GoodTypeVO;
import tech.whaleeye.model.vo.SecondHandGood.BackGoodVO;
import tech.whaleeye.model.vo.SecondHandGood.BriefGoodVO;
import tech.whaleeye.model.vo.SecondHandGood.FullGoodVO;
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
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(goodId);
        FullGoodVO fullGoodVO = modelMapper.map(secondHandGood, FullGoodVO.class);
        fullGoodVO.setIsCollecting(MiscUtils.currentUserId() != null && secondHandGoodMapper.isCollecting(MiscUtils.currentUserId(), goodId));
        GoodType goodType = null;
        if (secondHandGood.getTypeId() != null)
            goodType = goodTypeMapper.getGoodTypeById(secondHandGood.getTypeId());
        if (goodType != null)
            fullGoodVO.setGoodTypeVO(modelMapper.map(goodType, GoodTypeVO.class));
        fullGoodVO.setPicturePathList(goodPictureMapper.getPicturesByGoodId(fullGoodVO.getId()));
        return fullGoodVO;
    }

    @Override
    public BriefGoodVO getBriefGoodById(Integer goodId) {
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(goodId);
        BriefGoodVO briefGoodVO = modelMapper.map(secondHandGood, BriefGoodVO.class);
        GoodType goodType = null;
        if (secondHandGood.getTypeId() != null)
            goodType = goodTypeMapper.getGoodTypeById(secondHandGood.getTypeId());
        if (goodType != null)
            briefGoodVO.setGoodTypeVO(modelMapper.map(goodType, GoodTypeVO.class));
        briefGoodVO.setMainPicPath(goodPictureMapper.getMainPicPathByGoodId(briefGoodVO.getId()));
        return briefGoodVO;
    }

    @Override
    public PageList<BriefGoodVO> listAllGoods(Integer pageSize, Integer pageNo, Integer typeId, String searchKeyword) {
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
        return new PageList<>(briefGoodVOList, pageSize, pageNo, total);
    }

    @Override
    public PageList<BriefGoodVO> getGoodsByPublisher(Integer publisher, Integer pageSize, Integer pageNo, Boolean sold, String searchKeyword) {
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
        return new PageList<>(briefGoodVOList, pageSize, pageNo, total);
    }

    @Override
    public PageList<BriefGoodVO> listCollectedGoods(Integer userId, Integer pageSize, Integer pageNo) {
        List<BriefGoodVO> briefGoodVOList = new ArrayList<>();
        List<SecondHandGood> secondHandGoodList = secondHandGoodMapper.listCollectedGoods(userId, pageSize, (pageNo - 1) * pageSize);
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
        int total = secondHandGoodMapper.countCollectedGoods(userId);
        return new PageList<>(briefGoodVOList, pageSize, pageNo, total);
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
        StoreUser storeUser = storeUserMapper.getUserById(MiscUtils.currentUserId());
        if (storeUser.getCreditScore().doubleValue() < 85 || storeUser.getCardNumber() == null) {
            throw new LowCreditException();
        } else if (secondHandGoodDTO.getPrice().doubleValue() < 0f || secondHandGoodDTO.getPicturePathList().isEmpty()) {
            throw new InvalidValueException();
        }
        SecondHandGood secondHandGood = modelMapper.map(secondHandGoodDTO, SecondHandGood.class);
        secondHandGood.setPublisher(MiscUtils.currentUserId());
        if (secondHandGoodMapper.insertSecondHandGood(secondHandGood) <= 0) {
            return false;
        }
        return goodPictureMapper.insertGoodPictures(secondHandGood.getId(), secondHandGoodDTO.getPicturePathList()) > 0;
    }

    @Override
    public Boolean collectGood(Integer goodId) {
        return secondHandGoodMapper.collectGood(MiscUtils.currentUserId(), goodId) > 0;
    }

    @Override
    public Boolean cancelCollectGood(Integer goodId) {
        return secondHandGoodMapper.cancelCollectGood(MiscUtils.currentUserId(), goodId) > 0;
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
        return goodPictureMapper.insertGoodPictures(secondHandGoodDTO.getId(), secondHandGoodDTO.getPicturePathList()) > 0;
    }

    public Boolean deleteGood(Integer goodId, Integer userId) {
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(goodId);
        if (!secondHandGood.getPublisher().equals(userId)) {
            throw new BadIdentityException();
        }
        return secondHandGoodMapper.deleteSecondHandGood(goodId, userId);
    }

    @Override
    public PageList<BackGoodVO> listAllGoodsForBack(Integer pageSize, Integer pageNo, String searchNickname, String searchPhoneNumber, String searchKeyword) {
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
            backGoodVOList.add(backGoodVO);
        }
        int total = secondHandGoodMapper.countAllGoodsForBack(searchNickname, searchPhoneNumber, searchKeyword);
        return new PageList<>(backGoodVOList, pageSize, pageNo, total);
    }

    @Override
    public Boolean deleteGoodForBack(Integer goodId, Integer userId) {
        return secondHandGoodMapper.deleteSecondHandGood(goodId, userId);
    }
}
