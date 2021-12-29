package tech.whaleeye.service.Impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.CreditEventMapper;
import tech.whaleeye.mapper.CreditHistoryMapper;
import tech.whaleeye.mapper.StoreUserMapper;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.misc.exceptions.IllegalPasswordException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.CreditHistory;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.CreditSystem.CreditHistoryVO;
import tech.whaleeye.model.vo.StoreUser.BriefUserVO;
import tech.whaleeye.model.vo.StoreUser.StoreUserVO;
import tech.whaleeye.service.StoreUserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreUserServiceImpl implements StoreUserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StoreUserMapper storeUserMapper;

    @Autowired
    private CreditHistoryMapper creditHistoryMapper;

    @Autowired
    private CreditEventMapper creditEventMapper;


    @Override
    public StoreUser getStoreUserByPhoneNumber(String phoneNumber) {
        return storeUserMapper.getStoreUser(phoneNumber, null, null);
    }

    @Override
    public StoreUser getStoreUserByCardNumber(String cardNumber) {
        return storeUserMapper.getStoreUser(null, cardNumber, null);
    }

    @Override
    public StoreUser getStoreUserById(Integer userId) {
        return storeUserMapper.getStoreUser(null, null, userId);
    }

    @Override
    public PageList<BriefUserVO> listFollowers(Integer userId, Integer pageSize, Integer pageNo) {
        List<BriefUserVO> followers = storeUserMapper.listFollowers(userId, pageSize, (pageNo - 1) * pageSize);
        int total = storeUserMapper.countFollowers(userId);
        return new PageList<>(followers, pageSize, pageNo, total);
    }

    @Override
    public Integer countFollowers(Integer userId) {
        return storeUserMapper.countFollowers(userId);
    }

    @Override
    public PageList<BriefUserVO> listFollowings(Integer userId, Integer pageSize, Integer pageNo) {
        List<BriefUserVO> followings = storeUserMapper.listFollowings(userId, pageSize, (pageNo - 1) * pageSize);
        int total = storeUserMapper.countFollowings(userId);
        return new PageList<>(followings, pageSize, pageNo, total);
    }

    @Override
    public Integer countFollowings(Integer userId) {
        return storeUserMapper.countFollowings(userId);
    }

    @Override
    public PageList<BriefUserVO> listCollectors(Integer goodId, Integer pageSize, Integer pageNo) {
        List<BriefUserVO> briefGoodVOList = storeUserMapper.listCollectors(goodId, pageSize, (pageNo - 1) * pageSize);
        int total = storeUserMapper.countCollectors(goodId);
        return new PageList<>(briefGoodVOList, pageSize, pageNo, total);
    }

    @Override
    public Boolean isFollowing(Integer followerId, Integer followedId) {
        if (followerId == null) {
            return false;
        }
        return storeUserMapper.isFollowing(followerId, followedId);
    }

    @Override
    public PageList<CreditHistoryVO> listCreditHistory(Integer pageSize, Integer pageNo) {
        List<CreditHistory> creditHistoryList = creditHistoryMapper.listByUser(MiscUtils.currentUserId(), pageSize, (pageNo - 1) * pageSize);
        List<CreditHistoryVO> creditHistoryVOList = new ArrayList<>();
        CreditHistoryVO creditHistoryVO;
        for (CreditHistory creditHistory : creditHistoryList) {
            creditHistoryVO = modelMapper.map(creditHistory, CreditHistoryVO.class);
            creditHistoryVO.setEventName(creditEventMapper.queryById(creditHistory.getEventId()).getEventName());
            creditHistoryVOList.add(creditHistoryVO);
        }
        int total = creditHistoryMapper.countByUser(MiscUtils.currentUserId());
        return new PageList<>(creditHistoryVOList, pageSize, pageNo, total);
    }

    @Override
    public Boolean registerStoreUser(String phoneNumber) {
        return storeUserMapper.registerStoreUser(phoneNumber) > 0;
    }

    @Override
    public Boolean followUser(Integer userId, Integer followedId) {
        return storeUserMapper.followUser(userId, followedId) > 0;
    }

    @Override
    public Boolean unfollowUser(Integer userId, Integer followedId) {
        return storeUserMapper.unfollowUser(userId, followedId) > 0;
    }

    @Override
    public Boolean updatePassword(Integer userId, String password) {
        if (!password.matches("[a-zA-Z0-9!@#$%^&*()_+\\-=,.<>?/\\\\|\\[\\]{}:;\"'`~]{6,20}")) {
            throw new IllegalPasswordException();
        }
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return storeUserMapper.updatePassword(userId, password, hexSalt) > 0;
    }

    @Override
    public Integer setAlipayAccount(Integer userId, String alipayAccount) {
        if (alipayAccount.length() > 50) {
            throw new InvalidValueException();
        }
        return storeUserMapper.updateAlipayAccount(userId, alipayAccount, true);
    }

    @Override
    public Integer updateAlipayAccount(Integer userId, String alipayAccount) {
        if (alipayAccount.length() > 50) {
            throw new InvalidValueException();
        }
        return storeUserMapper.updateAlipayAccount(userId, alipayAccount, false);
    }

    @Override
    public Boolean setCardNumber(Integer userId, String cardNumber) {
        return storeUserMapper.setCardNumber(userId, cardNumber) > 0;
    }

    @Override
    public Boolean updateIntroduction(Integer userId, String introduction) {
        if (introduction.length() > 255) {
            throw new InvalidValueException();
        }
        return storeUserMapper.updateIntroduction(userId, introduction) > 0;
    }

    @Override
    public Boolean updateNickname(Integer userId, String nickname) {
        if (nickname.length() > 20) {
            throw new InvalidValueException();
        }
        return storeUserMapper.updateNickname(userId, nickname) > 0;
    }

    @Override
    public Boolean updateSex(Integer userId, Boolean sex) {
        return storeUserMapper.updateSex(userId, sex) > 0;
    }

    @Override
    public Boolean updateNotifications(Integer userId, Boolean secondHandNotification, Boolean agentServiceNotification, Boolean apiTradeNotification) {
        return storeUserMapper.updateNotifications(userId, secondHandNotification, agentServiceNotification, apiTradeNotification) > 0;
    }

    @Override
    public Integer updateAvatar(Integer userId, String avatarPath) {
        return storeUserMapper.updateAvatar(userId, avatarPath);
    }

    @Override
    public Boolean deleteStoreUser(Integer userId) {
        if (storeUserMapper.getUserById(userId).getAccountBalance().doubleValue() > 0) {
            throw new InvalidValueException();
        }
        return storeUserMapper.deleteStoreUser(userId) > 0;
    }

    @Override
    public PageList<StoreUserVO> listAll(Integer pageSize, Integer pageNo, @Nullable String searchNickname, @Nullable String searchPhoneNumber) {
        List<StoreUser> storeUserList = storeUserMapper.listAll(pageSize, (pageNo - 1) * pageSize, searchNickname, searchPhoneNumber);
        List<StoreUserVO> storeUserVOList = modelMapper.map(storeUserList, new TypeToken<List<StoreUserVO>>() {
        }.getType());
        int total = storeUserMapper.countAll(searchNickname, searchPhoneNumber);
        return new PageList<>(storeUserVOList, pageSize, pageNo, total);
    }

    @Override
    public Boolean banUser(Integer userId) {
        return storeUserMapper.banUser(userId) > 0;
    }

    @Override
    public Boolean unbanUser(Integer userId) {
        return storeUserMapper.unbanUser(userId) > 0;
    }

}