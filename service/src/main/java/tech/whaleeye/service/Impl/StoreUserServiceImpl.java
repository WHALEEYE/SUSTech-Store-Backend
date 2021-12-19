package tech.whaleeye.service.Impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.StoreUserMapper;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.misc.exceptions.IllegalPasswordException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.StoreUser.StoreUserVO;
import tech.whaleeye.service.StoreUserService;

import java.util.List;

@Service
public class StoreUserServiceImpl implements StoreUserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StoreUserMapper storeUserMapper;

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
    public Integer registerStoreUser(String phoneNumber) {
        return storeUserMapper.registerStoreUser(phoneNumber);
    }

    @Override
    public Integer followUser(Integer userId, Integer followedId) {
        return storeUserMapper.followUser(userId, followedId);
    }

    @Override
    public Boolean setPassword(Integer userId, String password) {
        if (!password.matches("[a-zA-Z0-9!@#$%^&*()_+\\-=,.<>?/\\\\|\\[\\]{}:;\"'`~]{6,20}")) {
            throw new IllegalPasswordException();
        }
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return storeUserMapper.updatePassword(userId, password, hexSalt, true) > 0;
    }

    @Override
    public Boolean updatePassword(Integer userId, String password) {
        if (!password.matches("[a-zA-Z0-9!@#$%^&*()_+\\-=,.<>?/\\\\|\\[\\]{}:;\"'`~]{6,20}")) {
            throw new IllegalPasswordException();
        }
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return storeUserMapper.updatePassword(userId, password, hexSalt, false) > 0;
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
    public Integer updateIntroduction(Integer userId, String introduction) {
        if (introduction.length() > 255) {
            throw new InvalidValueException();
        }
        return storeUserMapper.updateIntroduction(userId, introduction);
    }

    @Override
    public Integer updateNickname(Integer userId, String nickname) {
        if (nickname.length() > 20) {
            throw new InvalidValueException();
        }
        return storeUserMapper.updateNickname(userId, nickname);
    }

    @Override
    public Integer updateSex(Integer userId, Boolean sex) {
        return storeUserMapper.updateSex(userId, sex);
    }

    @Override
    public Integer updateNotifications(Integer userId, Boolean secondHandNotification, Boolean agentServiceNotification, Boolean apiTradeNotification) {
        return storeUserMapper.updateNotifications(userId, secondHandNotification, agentServiceNotification, apiTradeNotification);
    }

    @Override
    public Integer updateAvatar(Integer userId, String avatarPath) {
        return storeUserMapper.updateAvatar(userId, avatarPath);
    }

    @Override
    public Boolean deleteStoreUser(Integer userId) {
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