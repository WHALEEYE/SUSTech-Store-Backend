package tech.whaleeye.service.Impl;

import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.StoreUserMapper;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.exceptions.VCodeLimitException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.misc.utils.TencentCloudUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.service.StoreUserService;

import java.time.LocalDate;
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
    public Integer setVCode(String phoneNumber, String vCode) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, TencentCloudUtils.V_CODE_EXPIRE_TIME_MIN - 1);
        StoreUser storeUser = storeUserMapper.getStoreUserByPhoneNumber(phoneNumber);
        if (storeUser == null) {
            if (storeUserMapper.registerStoreUser(phoneNumber) <= 0) {
                return 0;
            }
        } else if (storeUser.getBanned()) {
            throw new LockedAccountException();
        } else if (storeUser.getVCodeExpireTime() != null && storeUser.getVCodeExpireTime().after(cal.getTime())) {
            // The interval of two sending requests are smaller than 1 minute
            throw new VCodeLimitException();
        }
        cal.add(Calendar.MINUTE, 1);
        return storeUserMapper.setVCode(phoneNumber, vCode, cal.getTime());
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
        if (cardNumber.length() != 8) {
            throw new InvalidValueException();
        }
        Integer career = null, admissionYear = null;
        boolean validCard = true;
        switch (cardNumber.charAt(0)) {
            case '1':
            case '2':
                switch (cardNumber.charAt(3)) {
                    case '1':
                        career = 1;
                        break;
                    case '4':
                    case '5':
                    case '6':
                        career = 2;
                        break;
                    default:
                        validCard = false;
                        break;
                }
                admissionYear = Integer.parseInt("20" + cardNumber.substring(1, 3));
                break;
            case '3':
                career = 3;
                break;
            default:
                validCard = false;
                break;
        }
        if (!validCard || (admissionYear != null && admissionYear > LocalDate.now().getYear())) {
            throw new InvalidValueException();
        }
        return storeUserMapper.setCardNumber(userId, cardNumber, career, admissionYear);
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