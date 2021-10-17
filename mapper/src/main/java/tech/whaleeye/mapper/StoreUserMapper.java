package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import tech.whaleeye.model.entity.StoreUser;

import java.util.Date;

@Mapper
public interface StoreUserMapper {

    StoreUser getStoreUser(@Nullable @Param("phoneNumber") String phoneNumber,
                           @Nullable @Param("cardNumber") String cardNumber,
                           @Nullable @Param("userId") Integer userId);

    Integer registerStoreUser(String phoneNumber);

    Integer followUser(@Param("followerId") Integer userId, @Param("followedId") Integer followedId);

    Integer updatePassword(@Param("userId") Integer userId, @Param("password") String password, @Param("salt") String salt, @Param("firstTime") Boolean firstTime);

    Integer updateAlipayAccount(@Param("userId") Integer userId, @Param("alipayAccount") String alipayAccount, @Param("firstTime") Boolean firstTime);

    Integer setCardNumber(@Param("userId") Integer userId, @Param("cardNumber") String cardNumber);

    Integer updateIntroduction(@Param("userId") Integer userId, @Param("introduction") String introduction);

    Integer updateNickname(@Param("userId") Integer userId, @Param("nickname") String nickname);

    Integer updateSex(@Param("userId") Integer userId, @Param("sex") Boolean sex);

    Integer updateNotifications(@Param("userId") Integer userId,
                                @Param("secondHandNotification") Boolean secondHandNotification,
                                @Param("agentServiceNotification") Boolean agentServiceNotification,
                                @Param("apiTradeNotification") Boolean apiTradeNotification);

    Integer updateAvatar(@Param("userId") Integer userId, @Param("avatarPath") String avatarPath);

    void deleteStoreUser(@Param("userId") Integer userId, @Param("deleteUserId") Integer deleteUserId);
}