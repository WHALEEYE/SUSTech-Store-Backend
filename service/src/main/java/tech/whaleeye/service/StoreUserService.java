package tech.whaleeye.service;

import org.springframework.lang.Nullable;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.entity.CreditHistory;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.CreditSystem.CreditHistoryVO;
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

    PageList<BriefUserVO> listCollectors(Integer goodId, Integer pageSize, Integer pageNo);

    Boolean isFollowing(Integer followerId, Integer followedId);

    PageList<CreditHistoryVO> listCreditHistory(Integer pageSize, Integer pageNo);

    Boolean registerStoreUser(String phoneNumber);

    Boolean followUser(Integer userId, Integer followedId);

    Boolean unfollowUser(Integer userId, Integer followedId);

    Boolean updatePassword(Integer userId, String password);

    Boolean setCardNumber(Integer userId, String cardNumber);

    Boolean updateIntroduction(Integer userId, String introduction);

    Boolean updateNickname(Integer userId, String nickname);

    Integer setAlipayAccount(Integer userId, String alipayAccount);

    Integer updateAlipayAccount(Integer userId, String alipayAccount);

    Boolean updateSex(Integer userId, Boolean sex);

    Boolean updateNotifications(Integer userId, Boolean secondHandNotification, Boolean agentServiceNotification, Boolean apiTradeNotification);

    Integer updateAvatar(Integer userId, String avatarPath);

    Boolean deleteStoreUser(Integer userId);

    // Used in background system

    PageList<StoreUserVO> listAll(Integer pageSize, Integer pageNo, @Nullable String searchNickname, @Nullable String searchPhoneNumber);

    Boolean banUser(Integer userId);

    Boolean unbanUser(Integer userId);

}