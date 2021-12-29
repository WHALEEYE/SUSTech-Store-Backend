package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.StoreUser.BriefUserVO;

import java.util.List;

@Mapper
public interface StoreUserMapper {

    StoreUser getUserById(@Param("userId") Integer userId);

    StoreUser getStoreUser(@Nullable @Param("phoneNumber") String phoneNumber, @Nullable @Param("cardNumber") String cardNumber, @Nullable @Param("userId") Integer userId);

    List<BriefUserVO> listFollowers(@Param("userId") Integer userId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer countFollowers(@Param("userId") Integer userId);

    List<BriefUserVO> listFollowings(@Param("userId") Integer userId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer countFollowings(@Param("userId") Integer userId);

    List<BriefUserVO> listCollectors(@Param("goodId") Integer goodId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer countCollectors(@Param("goodId") Integer goodId);

    List<BriefUserVO> listFriends(@Param("userId") Integer userId);

    Boolean isFollowing(@Param("followerId") Integer followerId, @Param("followedId") Integer followedId);

    Integer registerStoreUser(String phoneNumber);

    Integer followUser(@Param("followerId") Integer userId, @Param("followedId") Integer followedId);

    Integer unfollowUser(@Param("followerId") Integer followerId, @Param("followedId") Integer followedId);

    Integer updatePassword(@Param("userId") Integer userId, @Param("password") String password, @Param("salt") String salt);

    Integer updateAlipayAccount(@Param("userId") Integer userId, @Param("alipayAccount") String alipayAccount, @Param("firstTime") Boolean firstTime);

    Integer setCardNumber(@Param("userId") Integer userId, @Param("cardNumber") String cardNumber);

    Integer updateIntroduction(@Param("userId") Integer userId, @Param("introduction") String introduction);

    Integer updateNickname(@Param("userId") Integer userId, @Param("nickname") String nickname);

    Integer updateSex(@Param("userId") Integer userId, @Param("sex") Boolean sex);

    Integer updateNotifications(@Param("userId") Integer userId, @Param("secondHandNotification") Boolean secondHandNotification, @Param("agentServiceNotification") Boolean agentServiceNotification, @Param("apiTradeNotification") Boolean apiTradeNotification);

    Integer updateAvatar(@Param("userId") Integer userId, @Param("avatarPath") String avatarPath);

    Integer deleteStoreUser(@Param("userId") Integer userId);

    // Used in background system
    List<StoreUser> listAll(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset, @Nullable @Param("searchNickname") String searchNickname, @Nullable @Param("searchPhoneNumber") String searchPhoneNumber);

    Integer countAll(@Nullable @Param("searchNickname") String searchNickname, @Nullable @Param("searchPhoneNumber") String searchPhoneNumber);

    Integer banUser(@Param("userId") Integer userId);

    Integer unbanUser(@Param("userId") Integer userId);

}