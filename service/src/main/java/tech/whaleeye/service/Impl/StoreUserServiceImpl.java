package tech.whaleeye.service.Impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.StoreUserMapper;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.constants.Values;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.exceptions.VCodeLimitException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.service.StoreUserService;

import java.util.Calendar;

@Service
public class StoreUserServiceImpl implements StoreUserService {

    @Autowired
    private StoreUserMapper storeUserMapper;


    @Override
    public StoreUser getStoreUserByPhoneNumber(String phoneNumber) {
        return storeUserMapper.getStoreUserByPhoneNumber(phoneNumber);
    }

    @Override
    public StoreUser getStoreUserByCardNumber(String cardNumber) {
        return storeUserMapper.getStoreUserByCardNumber(cardNumber);
    }

    @Override
    public StoreUser getStoreUserById(Integer userId) {
        return storeUserMapper.getStoreUserById(userId);
    }

    @Override
    public Integer registerStoreUser(String phoneNumber) {
        return storeUserMapper.registerStoreUser(phoneNumber);
    }

    @Override
    public Integer setVCode(String phoneNumber, String vCode, VCodeType vCodeType) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, Values.V_CODE_EXPIRE_TIME_MINUTES - 1);
        StoreUser storeUser = storeUserMapper.getStoreUserByPhoneNumber(phoneNumber);
        if (storeUser.getVCodeExpireTime() != null
                && storeUser.getVCodeType() == vCodeType.getTypeCode()
                && storeUser.getVCodeExpireTime().after(cal.getTime())) {
            // The interval of two sending requests are smaller than 1 minute
            throw new VCodeLimitException();
        }
        cal.add(Calendar.MINUTE, 1);
        return storeUserMapper.setVCode(phoneNumber, vCode, cal.getTime(), vCodeType.getTypeCode());
    }

    @Override
    public Integer followUser(Integer userId, Integer followedId) {
        return storeUserMapper.followUser(userId, followedId);
    }

    @Override
    public void clearVCode(String phoneNumber) {
        storeUserMapper.clearVCode(phoneNumber);
    }

    @Override
    public Integer updatePassword(Integer userId, String password, Boolean firstTime) {
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return storeUserMapper.updatePassword(userId, password, hexSalt, firstTime);
    }

    @Override
    public Integer updateAlipayAccount(Integer userId, String alipayAccount, Boolean firstTime) {
        if (alipayAccount.length() > 50) {
            throw new InvalidValueException();
        }
        return storeUserMapper.updateAlipayAccount(userId, alipayAccount, firstTime);
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