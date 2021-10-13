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
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.misc.utils.TencentCloudUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.service.StoreUserService;

import java.util.Random;

@RestController
@Api("Miscellaneous Functions")
@RequestMapping("/misc")
public class MiscController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StoreUserService storeUserService;

    @ApiOperation("send verification code after login")
    @PostMapping("/vCode/{vCodeType}")
    public AjaxResult sendUserVCode(@PathVariable("vCodeType") Integer vCodeType) {
        StoreUser storeUser = storeUserService.getStoreUserById(MiscUtils.currentUserId());
        String phoneNumber = storeUser.getPhoneNumber();
        String vCode = String.format("%06d", new Random().nextInt(1000000));
        try {
            if (storeUserService.setVCode(phoneNumber, vCode, VCodeType.values()[vCodeType]) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
            if (!TencentCloudUtils.sendVCode(phoneNumber, vCode, VCodeType.values()[vCodeType])) {
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
    @PostMapping("/emailVCode/{cardNumber}")
    public AjaxResult sendEmailVCode(@PathVariable("cardNumber") Integer cardNumber) {
        // TODO: send email of verification code to <cardNumber>@mail.sustech.edu.cn
        // TODO: set verification code
        return null;
    }
}
