package tech.whaleeye.service.Impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.StoreUserMapper;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.service.StoreUserService;

@Service
public class StoreUserServiceImpl implements StoreUserService {

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
    public Integer setPassword(Integer userId, String password) {
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return storeUserMapper.updatePassword(userId, password, hexSalt, true);
    }

    @Override
    public Integer updatePassword(Integer userId, String password) {
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return storeUserMapper.updatePassword(userId, password, hexSalt, false);
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
    public Integer setCardNumber(Integer userId, String cardNumber) {
        return storeUserMapper.setCardNumber(userId, cardNumber);
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
    public void deleteStoreUser(Integer userId, Integer deleteUserId) {
        storeUserMapper.deleteStoreUser(userId, deleteUserId);
    }
}