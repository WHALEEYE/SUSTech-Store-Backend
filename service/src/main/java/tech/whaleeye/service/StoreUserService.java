package tech.whaleeye.service;

import tech.whaleeye.model.entity.StoreUser;

public interface StoreUserService {

    StoreUser getStoreUserByPhoneNumber(String phoneNumber);

    StoreUser getStoreUserByCardNumber(String cardNumber);

    StoreUser getStoreUserById(Integer userId);

    Integer setVCode(String phoneNumber, String vCode);

    void clearVCode(String phoneNumber);

    Integer updatePassword(Integer userId, String password, Boolean firstTime);

    Integer setCardNumber(Integer userId, String cardNumber);

    Integer updateIntroduction(Integer userId, String introduction);

    Integer updateNickname(Integer userId, String nickname);

    Integer updateAlipayAccount(Integer userId, String alipayAccount, Boolean firstTime);

    Integer updateSex(Integer userId, Boolean sex);

    Integer updateNotifications(Integer userId, Boolean secondHandNotification, Boolean agentServiceNotification, Boolean apiTradeNotification);

    Integer updateAvatar(Integer userId, String avatarPath);

    void deleteStoreUser(Integer userId, Integer deleteUserId);
}