package tech.whaleeye.frontcontroller.controllers;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.frontcontroller.config.shiro.LoginToken;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.constants.Values;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.exceptions.VCodeLimitException;
import tech.whaleeye.misc.utils.JWTUtils;
import tech.whaleeye.misc.utils.TencentCloudUtils;
import tech.whaleeye.service.StoreUserService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@RestController
@ApiOperation("Login Controller")
@RequestMapping("/login")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    StoreUserService storeUserService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation("login by phone number")
    @PostMapping("/phone")
    public AjaxResult phoneLogin(ServletResponse response, String phoneNumber, String VCode) {
        if (phoneNumber.length() != 11) {
            return AjaxResult.setSuccess(false).setMsg("Invalid phone number.");
        }
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        Subject subject = SecurityUtils.getSubject();
        LoginToken token = new LoginToken(phoneNumber, VCode, LoginToken.LoginType.PHONE_CODE);
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            return AjaxResult.setSuccess(false).setMsg("This phone number doesn't have corresponding accounts.");
        } catch (IncorrectCredentialsException ice) {
            return AjaxResult.setSuccess(false).setMsg("Verify code incorrect or expired.");
        } catch (LockedAccountException lae) {
            return AjaxResult.setSuccess(false).setMsg("This account is banned.");
        } catch (AuthenticationException ae) {
            return AjaxResult.setSuccess(false).setMsg("Unknown problem.");
        }
        // If login success
        // Clear the verification code
        storeUserService.clearVCode(phoneNumber);

        // Give the token to the user
        String jwtToken = JWTUtils.sign(subject.getPrincipal().toString(), Values.JWT_SECRET);
        httpResponse.setHeader(Values.JWT_AUTH_HEADER, jwtToken);

        return AjaxResult.setSuccess(true).setMsg("Login success.");

    }

    @ApiOperation("login by card number")
    @PostMapping("/card")
    public AjaxResult cardLogin(ServletResponse response, String cardNumber, String password) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        Subject subject = SecurityUtils.getSubject();
        LoginToken token = new LoginToken(cardNumber, password, LoginToken.LoginType.CARD_PWD);
        try {
            subject.login(token);
        } catch (UnknownAccountException uae) {
            return AjaxResult.setSuccess(false).setMsg("This card number doesn't have corresponding accounts.");
        } catch (IncorrectCredentialsException ice) {
            return AjaxResult.setSuccess(false).setMsg("Wrong password.");
        } catch (LockedAccountException lae) {
            return AjaxResult.setSuccess(false).setMsg("This account is banned.");
        } catch (AuthenticationException ae) {
            return AjaxResult.setSuccess(false).setMsg("Unknown problem.");
        }
        // If login success, give the token to user
        String jwtToken = JWTUtils.sign(subject.getPrincipal().toString(), Values.JWT_SECRET);
        httpResponse.setHeader(Values.JWT_AUTH_HEADER, jwtToken);
        return AjaxResult.setSuccess(true).setMsg("Logged in successfully.");
    }

    @ApiOperation("send verification code")
    @GetMapping("/vCode/{phoneNumber}")
    public AjaxResult sendVCode(@PathVariable("phoneNumber") String phoneNumber) {
        if (phoneNumber.length() != 11) {
            return AjaxResult.setSuccess(false).setMsg("Invalid phone number.");
        }
        String vCode = String.format("%06d", new Random().nextInt(1000000));
        try {
            if (storeUserService.setVCode(phoneNumber, vCode, VCodeType.LOGIN) <= 0) {
                log.error("Phone number [" + phoneNumber + "]: verification code failed to set.");
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
            if (!TencentCloudUtils.sendVCode(phoneNumber, vCode, VCodeType.LOGIN)) {
                log.error("Phone number [" + phoneNumber + "]: verification code failed to send.");
                return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
            }
        } catch (TencentCloudSDKException e) {
            log.error("Phone number [" + phoneNumber + "]: verification code failed to send (internal error).");
            return AjaxResult.setSuccess(false).setMsg("Failed to send verification code. Please try again later or contact with the administrator.");
        } catch (LockedAccountException e) {
            return AjaxResult.setSuccess(false).setMsg("The account is banned.");
        } catch (VCodeLimitException e) {
            return AjaxResult.setSuccess(false).setMsg("The request is too often.");
        }
        return AjaxResult.setSuccess(true).setMsg("Success");
    }
}
