package tech.whaleeye.frontcontroller.controllers;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.exceptions.VCodeLimitException;
import tech.whaleeye.misc.utils.EmailUtils;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.misc.utils.TencentCloudUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.service.StoreUserService;
import tech.whaleeye.service.VCodeRecordService;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.Random;

@Api("Verification Code Sending")
@RestController
@RequestMapping("/vCode")
@Log4j2
public class VCodeRecordController {

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private VCodeRecordService vCodeRecordService;

    @ApiOperation("send verification code for login")
    @PostMapping("/login/{phoneNumber}")
    AjaxResult sendLoginVCode(@PathVariable("phoneNumber") String phoneNumber) {
        if (!phoneNumber.matches("[0-9]{11}")) {
            return AjaxResult.setSuccess(false).setMsg("Invalid phone number.");
        }
        if (storeUserService.getStoreUserByPhoneNumber(phoneNumber) == null) {
            if (!storeUserService.registerStoreUser(phoneNumber)) {
                return AjaxResult.setSuccess(false).setMsg("Failed to register. Please try again later or contact with the administrator.");
            }
        }
        String vCode = String.format("%06d", new Random().nextInt(1000000));
        try {
            if (vCodeRecordService.setLoginVCode(phoneNumber, vCode) <= 0) {
                log.error("Phone number [" + phoneNumber + "]: verification code failed to set.");
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
            TencentCloudUtils.sendVCodeSMS(phoneNumber, vCode, VCodeType.LOGIN);
        } catch (TencentCloudSDKException e) {
            log.error("Phone number [" + phoneNumber + "]: verification code failed to send (internal error).");
            return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
        } catch (VCodeLimitException e) {
            return AjaxResult.setSuccess(false).setMsg("The request is too often.");
        }
        return AjaxResult.setSuccess(true).setMsg("Success");
    }

    @ApiOperation("send verification code after login")
    @PostMapping("/account/{vCodeType}")
    AjaxResult sendAccountVCode(@PathVariable("vCodeType")
                                @ApiParam("1: change password; 2: change alipay; 3: cancel account") Integer vCodeType) {
        StoreUser storeUser = storeUserService.getStoreUserById(MiscUtils.currentUserId());
        String vCode = String.format("%06d", new Random().nextInt(1000000));
        try {
            if (vCodeRecordService.setAccountVCode(storeUser.getId(), vCode, VCodeType.values()[vCodeType]) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
            TencentCloudUtils.sendVCodeSMS(storeUser.getPhoneNumber(), vCode, VCodeType.values()[vCodeType]);
        } catch (TencentCloudSDKException e) {
            log.error("User ID [" + MiscUtils.currentUserId() + "]: verification code failed to send.");
            return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
        } catch (VCodeLimitException e) {
            return AjaxResult.setSuccess(false).setMsg("The request is too often.");
        }
        return AjaxResult.setSuccess(true).setMsg("Success.");
    }

    @ApiOperation("send verification code to email")
    @PostMapping("/email/{cardNumber}")
    AjaxResult sendEmailVCode(@PathVariable("cardNumber") String cardNumber,
                              @RequestParam String postfix) {
        String receiveEmail = cardNumber.concat(postfix);
        String vCode = String.format("%06d", new Random().nextInt(1000000));
        try {
            if (vCodeRecordService.setEmailVCode(MiscUtils.currentUserId(), cardNumber, vCode) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
            EmailUtils.sendVCodeEmail(receiveEmail, vCode);
        } catch (GeneralSecurityException | MessagingException e) {
            log.error("User ID [" + MiscUtils.currentUserId() + "]: verification code mail failed to send (Internal Error).");
            return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
        } catch (VCodeLimitException e) {
            return AjaxResult.setSuccess(false).setMsg("The request is too often.");
        }
        return AjaxResult.setSuccess(true).setMsg("Success.");
    }
}
