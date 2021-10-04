package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import tech.whaleeye.model.entity.StoreUser;

import java.util.Date;

@Mapper
public interface StoreUserMapper {

    StoreUser getStoreUserByPhoneNumber(String phoneNumber);

    StoreUser getStoreUserByCardNumber(String cardNumber);

    StoreUser getStoreUserById(Integer userId);

    Integer registerStoreUser(String phoneNumber);

    Integer followUser(@Param("followerId") Integer userId, @Param("followedId") Integer followedId);

    Integer setVCode(@Param("phoneNumber") String phoneNumber, @Param("vCode") String vCode, @Param("vCodeExpireTime") Date vCodeExpireTime, @Param("vCodeType") Integer vCodeType);

    void clearVCode(String phoneNumber);

    Integer updatePassword(@Param("userId") Integer userId, @Param("password") String password, @Param("salt") String salt, @Param("firstTime") Boolean firstTime);

    Integer updateAlipayAccount(@Param("userId") Integer userId, @Param("alipayAccount") String alipayAccount, @Param("firstTime") Boolean firstTime);

    Integer setCardNumber(@Param("userId") Integer userId, @Param("cardNumber") String cardNumber, @Param("career") Integer career, @Param("admissionYear") Integer admissionYear);

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