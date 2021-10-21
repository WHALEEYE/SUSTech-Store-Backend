package tech.whaleeye.frontcontroller.controllers;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
@Api("Verification Code Sending")
@RequestMapping("/vCode")
public class VCodeRecordController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StoreUserService storeUserService;

    @Autowired
    VCodeRecordService vCodeRecordService;

    @ApiOperation("send verification code for login")
    @PostMapping("/login/{phoneNumber}")
    public AjaxResult sendLoginVCode(@PathVariable("phoneNumber") String phoneNumber) {
        if (phoneNumber.length() != 11) {
            return AjaxResult.setSuccess(false).setMsg("Invalid phone number.");
        }
        if (storeUserService.getStoreUserByPhoneNumber(phoneNumber) == null) {
            if (storeUserService.registerStoreUser(phoneNumber) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to register. Please try again later or contact with the administrator.");
            }
        }
        String vCode = String.format("%06d", new Random().nextInt(1000000));
        try {
            if (vCodeRecordService.setLoginVCode(phoneNumber, vCode) <= 0) {
                log.error("Phone number [" + phoneNumber + "]: verification code failed to set.");
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
            if (!TencentCloudUtils.sendVCodeSMS(phoneNumber, vCode, VCodeType.LOGIN)) {
                log.error("Phone number [" + phoneNumber + "]: verification code failed to send.");
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
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
    public AjaxResult sendAccountVCode(@PathVariable("vCodeType") Integer vCodeType) {
        StoreUser storeUser = storeUserService.getStoreUserById(MiscUtils.currentUserId());
        String vCode = String.format("%06d", new Random().nextInt(1000000));
        try {
            if (vCodeRecordService.setAccountVCode(storeUser.getId(), vCode, VCodeType.values()[vCodeType]) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
            if (!TencentCloudUtils.sendVCodeSMS(storeUser.getPhoneNumber(), vCode, VCodeType.values()[vCodeType])) {
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
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
    public AjaxResult sendEmailVCode(@PathVariable("cardNumber") String cardNumber) {
        String receiveEmail = cardNumber.concat("@mail.sustech.edu.cn");
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