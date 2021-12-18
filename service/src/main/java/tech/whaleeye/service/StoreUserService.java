package tech.whaleeye.service;

import org.springframework.lang.Nullable;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.StoreUser.StoreUserVO;

public interface StoreUserService {

    StoreUser getStoreUserByPhoneNumber(String phoneNumber);

    StoreUser getStoreUserByCardNumber(String cardNumber);

    StoreUser getStoreUserById(Integer userId);

    Integer registerStoreUser(String phoneNumber);

    Integer followUser(Integer userId, Integer followedId);

    Integer setPassword(Integer userId, String password);

    Integer updatePassword(Integer userId, String password);

    Integer setCardNumber(Integer userId, String cardNumber);

    Integer updateIntroduction(Integer userId, String introduction);

    Integer updateNickname(Integer userId, String nickname);

    Integer setAlipayAccount(Integer userId, String alipayAccount);

    Integer updateAlipayAccount(Integer userId, String alipayAccount);

    Integer updateSex(Integer userId, Boolean sex);

    Integer updateNotifications(Integer userId, Boolean secondHandNotification, Boolean agentServiceNotification, Boolean apiTradeNotification);

    Integer updateAvatar(Integer userId, String avatarPath);

    Boolean deleteStoreUser(Integer userId);

    // Used in background system
    PageList<StoreUserVO> listAll(Integer pageSize, Integer pageNo, @Nullable String searchNickname, @Nullable String searchPhoneNumber);

    Boolean banUser(Integer userId);

    Boolean unbanUser(Integer userId);

}