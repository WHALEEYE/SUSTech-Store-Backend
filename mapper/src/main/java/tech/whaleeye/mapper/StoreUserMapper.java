package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import tech.whaleeye.model.entity.StoreUser;

import java.util.List;

@Mapper
public interface StoreUserMapper {

    StoreUser getUserById(@Param("userId") Integer userId);

    StoreUser getStoreUser(@Nullable @Param("phoneNumber") String phoneNumber, @Nullable @Param("cardNumber") String cardNumber, @Nullable @Param("userId") Integer userId);

    Integer registerStoreUser(String phoneNumber);

    Integer followUser(@Param("followerId") Integer userId, @Param("followedId") Integer followedId);

    Integer updatePassword(@Param("userId") Integer userId, @Param("password") String password, @Param("salt") String salt, @Param("firstTime") Boolean firstTime);

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