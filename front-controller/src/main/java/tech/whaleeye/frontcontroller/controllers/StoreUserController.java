package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.UploadFileType;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.exceptions.IllegalPasswordException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.entity.VCodeRecord;
import tech.whaleeye.model.vo.StoreUser.OtherUserVO;
import tech.whaleeye.model.vo.StoreUser.StoreUserVO;
import tech.whaleeye.service.SecondHandGoodService;
import tech.whaleeye.service.StoreUserService;
import tech.whaleeye.service.VCodeRecordService;

import java.io.IOException;


@Api("Controls Store User Accounts and Relationships")
@RestController
@RequestMapping("/storeUser")
@Log4j2
public class StoreUserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private SecondHandGoodService secondHandGoodService;

    @Autowired
    private VCodeRecordService vCodeRecordService;

    @ApiOperation("get current user's information")
    @GetMapping("/info")
    AjaxResult getUserInfo() {
        try {
            StoreUser storeUser = storeUserService.getStoreUserById(MiscUtils.currentUserId());
            if (storeUser == null) {
                return AjaxResult.setSuccess(false).setMsg("No user found!");
            } else {
                StoreUserVO storeUserVO = modelMapper.map(storeUser, StoreUserVO.class);
                storeUserVO.setFollowerCount(storeUserService.countFollowers(MiscUtils.currentUserId()));
                storeUserVO.setFollowingCount(storeUserService.countFollowings(MiscUtils.currentUserId()));
                return AjaxResult.setSuccess(true).setData(storeUserVO);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to get user info");
        }
    }

    @ApiOperation("get other user's information")
    @GetMapping("/info/{userId}")
    AjaxResult getUserInfo(@PathVariable("userId") Integer userId) {
        try {
            StoreUser otherUser = storeUserService.getStoreUserById(userId);
            if (otherUser == null) {
                return AjaxResult.setSuccess(false).setMsg("Failed to fetch user info.");
            }
            if (userId.equals(MiscUtils.currentUserId())) {
                StoreUserVO storeUserVO = modelMapper.map(otherUser, StoreUserVO.class);
                storeUserVO.setFollowerCount(storeUserService.countFollowers(userId));
                storeUserVO.setFollowingCount(storeUserService.countFollowings(userId));
                return AjaxResult.setSuccess(true).setData(storeUserVO);
            } else {
                OtherUserVO otherUserVO = modelMapper.map(otherUser, OtherUserVO.class);
                otherUserVO.setFollowerCount(storeUserService.countFollowers(userId));
                otherUserVO.setFollowingCount(storeUserService.countFollowings(userId));
                otherUserVO.setPhoneNumber(otherUser.getPhoneNumber().substring(0, 3) + "****" + otherUser.getPhoneNumber().substring(8));
                return AjaxResult.setSuccess(true).setData(otherUserVO);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to fetch user info.");
        }
    }

    @ApiOperation("get current user's followers")
    @GetMapping("/followers")
    AjaxResult getFollowers(@RequestParam Integer pageSize,
                            @RequestParam Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(storeUserService.listFollowers(MiscUtils.currentUserId(), pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list the followers");
        }
    }

    @ApiOperation("get other user's followers")
    @GetMapping("/followers/{userId}")
    AjaxResult getFollowers(@PathVariable("userId") Integer userId,
                            @RequestParam Integer pageSize,
                            @RequestParam Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(storeUserService.listFollowers(userId, pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list the followers");
        }
    }

    @ApiOperation("get current user's followings")
    @GetMapping("/followings")
    AjaxResult getFollowings(@RequestParam Integer pageSize,
                             @RequestParam Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(storeUserService.listFollowings(MiscUtils.currentUserId(), pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list the followers");
        }
    }

    @ApiOperation("get other user's followings")
    @GetMapping("/followings/{userId}")
    AjaxResult getFollowings(@PathVariable("userId") Integer userId,
                             @RequestParam Integer pageSize,
                             @RequestParam Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(storeUserService.listFollowings(userId, pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list the followers");
        }
    }

    @ApiOperation("get current user's collected goods")
    @GetMapping("/collected")
    AjaxResult getCollected(@RequestParam Integer pageSize,
                            @RequestParam Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.listCollectedGoods(MiscUtils.currentUserId(), pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(true).setMsg("Failed to list collected goods");
        }
    }

    @ApiOperation("get other user's collected goods")
    @GetMapping("/collected/{userId}")
    AjaxResult getCollected(@PathVariable("userId") Integer userId,
                            @RequestParam Integer pageSize,
                            @RequestParam Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.listCollectedGoods(userId, pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list collected goods");
        }
    }

    @ApiOperation("Follow One User")
    @PostMapping("/follow/{userId}")
    AjaxResult followUser(@PathVariable("userId") Integer userId) {
        try {
            if (storeUserService.followUser(MiscUtils.currentUserId(), userId)) {
                return AjaxResult.setSuccess(true).setMsg("Followed Successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to Follow");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to Follow");
        }
    }

    @ApiOperation("Unfollow One User")
    @DeleteMapping("/unfollow/{userId}")
    AjaxResult unfollowUser(@PathVariable("userId") Integer userId) {
        try {
            if (storeUserService.unfollowUser(MiscUtils.currentUserId(), userId)) {
                return AjaxResult.setSuccess(true).setMsg("Unfollowed Successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to unfollow");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to unfollow");
        }
    }

    @ApiOperation("set user password")
    @PostMapping("/password")
    AjaxResult setPassword(@RequestParam String password) {
        try {
            if (storeUserService.setPassword(MiscUtils.currentUserId(), password)) {
                return AjaxResult.setSuccess(true).setMsg("Success.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
        } catch (IllegalPasswordException ipe) {
            return AjaxResult.setSuccess(false).setMsg("This password is illegal");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
        }
    }

    @ApiOperation("set alipay account")
    @PostMapping("/alipay")
    AjaxResult setAlipayAccount(@RequestParam String alipayAccount) {
        if (storeUserService.setAlipayAccount(MiscUtils.currentUserId(), alipayAccount) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to set alipay account.");
        }
        return AjaxResult.setSuccess(true).setMsg("Success.");
    }

    @ApiOperation("set card number")
    @PostMapping("/cardNumber")
    AjaxResult setCardNumber(@RequestParam String cardNumber,
                             @RequestParam String vCode) {
        try {
            VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailEmailVCode(MiscUtils.currentUserId(), cardNumber);
            if (vCodeRecord == null || !vCodeRecord.getVCode().equals(vCode)) {
                return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired.");
            }
            if (!storeUserService.setCardNumber(MiscUtils.currentUserId(), cardNumber)) {
                return AjaxResult.setSuccess(false).setMsg("Failed to set card number");
            }
            vCodeRecordService.setVCodeUsed(vCodeRecord.getId());
            return AjaxResult.setSuccess(true).setMsg("Card number set successfully.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to set card number");
        }
    }

    @ApiOperation("update password")
    @PatchMapping("/password")
    AjaxResult updatePassword(@RequestParam String vCode,
                              @RequestParam String newPassword) {
        try {
            VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailAccountVCode(MiscUtils.currentUserId(), VCodeType.CHANGE_PASSWORD);
            if (vCodeRecord == null || !vCodeRecord.getVCode().equals(vCode)) {
                return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired.");
            }
            if (!storeUserService.updatePassword(MiscUtils.currentUserId(), newPassword)) {
                return AjaxResult.setSuccess(false).setMsg("Failed to update password.");
            }
            vCodeRecordService.setVCodeUsed(vCodeRecord.getId());
            return AjaxResult.setSuccess(true).setMsg("Password has updated successfully.");
        } catch (IllegalPasswordException ipe) {
            return AjaxResult.setSuccess(false).setMsg("This password is illegal");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to update password.");
        }
    }

    @ApiOperation("update alipay account")
    @PatchMapping("/alipay")
    AjaxResult updateAlipayAccount(@RequestParam String vCode,
                                   @RequestParam String alipayAccount) {
        VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailAccountVCode(MiscUtils.currentUserId(), VCodeType.CHANGE_ALIPAY);
        if (vCodeRecord == null || !vCodeRecord.getVCode().equals(vCode)) {
            return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired.");
        }
        if (storeUserService.updateAlipayAccount(MiscUtils.currentUserId(), alipayAccount) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to update alipay account.");
        }
        vCodeRecordService.setVCodeUsed(vCodeRecord.getId());
        return AjaxResult.setSuccess(true).setMsg("Alipay account has updated successfully.");
    }

    @ApiOperation("update self introduction")
    @PatchMapping("/intro")
    AjaxResult updateIntroduction(@RequestParam String introduction) {
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
    AjaxResult updateNickname(@RequestParam String nickname) {
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
    AjaxResult updateNickname(@RequestParam Boolean sex) {
        if (storeUserService.updateSex(MiscUtils.currentUserId(), sex) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to update sex information.");
        }
        return AjaxResult.setSuccess(true).setMsg("Sex information updated successfully.");
    }

    @ApiOperation("update notifications")
    @PatchMapping("/notifications")
    AjaxResult updateNotifications(@RequestParam Boolean secondHandNotification,
                                   @RequestParam Boolean agentServiceNotification,
                                   @RequestParam Boolean apiTradeNotification) {
        if (storeUserService.updateNotifications(MiscUtils.currentUserId(), secondHandNotification, agentServiceNotification, apiTradeNotification) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to update notification settings.");
        }
        return AjaxResult.setSuccess(true).setMsg("Notification settings updated successfully.");
    }

    @ApiOperation("update user avatar")
    @PatchMapping("/avatar")
    AjaxResult updateAvatar(@RequestPart MultipartFile avatar) {
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

    @ApiOperation("cancel user account")
    @DeleteMapping("/cancel")
    AjaxResult cancelAccount(@RequestParam String vCode) {
        try {
            VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailAccountVCode(MiscUtils.currentUserId(), VCodeType.CANCEL_ACCOUNT);
            if (vCodeRecord == null || !vCodeRecord.getVCode().equals(vCode)) {
                return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired");
            }
            vCodeRecordService.setVCodeUsed(vCodeRecord.getId());
            if (storeUserService.deleteStoreUser(MiscUtils.currentUserId())) {
                return AjaxResult.setSuccess(true).setMsg("Account has cancelled successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to cancel account");
        } catch (InvalidValueException ive) {
            return AjaxResult.setSuccess(false).setMsg("There are balance left in the account");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to cancel account");
        }
    }
}
