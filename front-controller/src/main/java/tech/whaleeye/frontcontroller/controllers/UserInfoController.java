package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.UploadFileType;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.entity.VCodeRecord;
import tech.whaleeye.model.vo.OtherUserVO;
import tech.whaleeye.model.vo.StoreUserVO;
import tech.whaleeye.service.StoreUserService;
import tech.whaleeye.service.VCodeRecordService;

import java.io.IOException;

@Api("Check and Modify User Information")
@RequestMapping("/userInfo")
@RestController
public class UserInfoController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StoreUserService storeUserService;

    @Autowired
    VCodeRecordService vCodeRecordService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation("get other user's information")
    @GetMapping("/info/{userId}")
    public AjaxResult getOtherInfo(@PathVariable("userId") Integer userId) {
        StoreUser otherUser = storeUserService.getStoreUserById(userId);
        if (otherUser == null) {
            return AjaxResult.setSuccess(false).setMsg("Error in fetching user info.");
        } else {
            if (userId.equals(MiscUtils.currentUserId())) {
                return AjaxResult.setSuccess(true).setData(modelMapper.map(otherUser, StoreUserVO.class));
            } else {
                OtherUserVO otherUserVO = modelMapper.map(otherUser, OtherUserVO.class);
                otherUserVO.setPhoneNumber(otherUser.getPhoneNumber().substring(0, 3) + "****" + otherUser.getPhoneNumber().substring(8));
                return AjaxResult.setSuccess(true).setData(otherUserVO);
            }
        }
    }

    @ApiOperation("get current user's information")
    @GetMapping("/info")
    public AjaxResult getInfo() {
        StoreUser storeUser = storeUserService.getStoreUserById(MiscUtils.currentUserId());
        if (storeUser == null) {
            return AjaxResult.setSuccess(false).setMsg("No corresponding user found!");
        } else {
            return AjaxResult.setSuccess(true).setData(modelMapper.map(storeUser, StoreUserVO.class));
        }
    }

    @ApiOperation("set user password")
    @PutMapping("/password")
    public AjaxResult setPassword(String password) {
        if (storeUserService.updatePassword(MiscUtils.currentUserId(), password, true) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
        }
        return AjaxResult.setSuccess(true).setMsg("Success.");
    }

    @ApiOperation("set alipay account")
    @PutMapping("/alipay")
    public AjaxResult setAlipayAccount(String alipayAccount) {
        if (storeUserService.updateAlipayAccount(MiscUtils.currentUserId(), alipayAccount, true) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to set alipay account.");
        }
        return AjaxResult.setSuccess(true).setMsg("Success.");
    }

    @ApiOperation("set card number")
    @PutMapping("/cardNumber")
    public AjaxResult setCardNumber(String cardNumber, String vCode) {
        // TODO: Find a way to verify that the card number is the same with the email receiving the v code
        if (storeUserService.setCardNumber(MiscUtils.currentUserId(), cardNumber) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to set card number");
        }
        return AjaxResult.setSuccess(true).setMsg("Card number set successfully.");
    }

    @ApiOperation("update password")
    @PatchMapping("/password")
    public AjaxResult updatePassword(String vCode, String newPassword) {
        VCodeRecord vCodeRecord = vCodeRecordService.getLatestAccountVCode(MiscUtils.currentUserId(), VCodeType.CHANGE_PASSWORD);
        if (vCodeRecord == null || !vCodeRecord.getVCode().equals(vCode)) {
            return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired.");
        }
        if (storeUserService.updatePassword(MiscUtils.currentUserId(), newPassword, false) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to update password.");
        }
        vCodeRecordService.setVCodeUsed(vCodeRecord.getId());
        return AjaxResult.setSuccess(true).setMsg("Password has updated successfully.");
    }

    @ApiOperation("update alipay account")
    @PatchMapping("/alipay")
    public AjaxResult updateAlipayAccount(String vCode, String alipayAccount) {
        VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailAccountVCode(MiscUtils.currentUserId(), VCodeType.CHANGE_ALIPAY);
        if (vCodeRecord == null || !vCodeRecord.getVCode().equals(vCode)) {
            return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired.");
        }
        if (storeUserService.updateAlipayAccount(MiscUtils.currentUserId(), alipayAccount, false) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to update alipay account.");
        }
        vCodeRecordService.setVCodeUsed(vCodeRecord.getId());
        return AjaxResult.setSuccess(true).setMsg("Alipay account has updated successfully.");
    }

    @ApiOperation("update self introduction")
    @PatchMapping("/intro")
    public AjaxResult updateIntroduction(String introduction) {
        try {
            if (storeUserService.updateIntroduction(MiscUtils.currentUserId(), introduction) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to update introduction.");
            }
        } catch (InvalidValueException e) {
            return AjaxResult.setSuccess(false).setMsg("Introduction too long.");
        }
        return AjaxResult.setSuccess(true).setMsg("Introduction updated successfully.");
    }

    @ApiOperation("update nickname")
    @PatchMapping("/nickname")
    public AjaxResult updateNickname(String nickname) {
        try {
            if (storeUserService.updateNickname(MiscUtils.currentUserId(), nickname) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to update nickname.");
            }
        } catch (InvalidValueException e) {
            return AjaxResult.setSuccess(false).setMsg("Nickname too long.");
        }
        return AjaxResult.setSuccess(true).setMsg("Nickname updated successfully.");
    }

    @ApiOperation("update sex")
    @PatchMapping("/sex")
    public AjaxResult updateNickname(Boolean sex) {
        if (storeUserService.updateSex(MiscUtils.currentUserId(), sex) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to update sex information.");
        }
        return AjaxResult.setSuccess(true).setMsg("Sex information updated successfully.");
    }

    @ApiOperation("update notifications")
    @PatchMapping("/notifications")
    public AjaxResult updateNotifications(Boolean secondHandNotification, Boolean agentServiceNotification, Boolean apiTradeNotification) {
        if (storeUserService.updateNotifications(MiscUtils.currentUserId(), secondHandNotification, agentServiceNotification, apiTradeNotification) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to update notification settings.");
        }
        return AjaxResult.setSuccess(true).setMsg("Notification settings updated successfully.");
    }

    @ApiOperation("update user avatar")
    @PatchMapping("/avatar")
    public AjaxResult updateAvatar(@RequestPart MultipartFile avatar) {
        try {
            String avatarPath = MiscUtils.processPicture(avatar, UploadFileType.AVATAR);
            if (storeUserService.updateAvatar(MiscUtils.currentUserId(), avatarPath) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to save avatar.");
            }
            return AjaxResult.setSuccess(true).setMsg("Avatar saved successfully.");
        } catch (IOException e) {
            log.error("User ID [" + MiscUtils.currentUserId() + "]: failed to save avatar.");
            return AjaxResult.setSuccess(false).setMsg("Failed to save avatar.");
        } catch (InvalidValueException e) {
            return AjaxResult.setSuccess(false).setMsg("Invalid avatar. It's not a picture or it's not a square.");
        }
    }
}
