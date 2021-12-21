package tech.whaleeye.service;

import org.springframework.lang.Nullable;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.StoreUser.BriefUserVO;
import tech.whaleeye.model.vo.StoreUser.StoreUserVO;

public interface StoreUserService {

    StoreUser getStoreUserByPhoneNumber(String phoneNumber);

    StoreUser getStoreUserByCardNumber(String cardNumber);

    StoreUser getStoreUserById(Integer userId);

    PageList<BriefUserVO> listFollowers(Integer userId, Integer pageSize, Integer pageNo);

    Integer countFollowers(Integer userId);

    PageList<BriefUserVO> listFollowings(Integer userId, Integer pageSize, Integer pageNo);

    Integer countFollowings(Integer userId);

    Boolean registerStoreUser(String phoneNumber);

    Boolean followUser(Integer userId, Integer followedId);

    Boolean unfollowUser(Integer userId, Integer followedId);

    Boolean setPassword(Integer userId, String password);

    Boolean updatePassword(Integer userId, String password);

    Boolean setCardNumber(Integer userId, String cardNumber);

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