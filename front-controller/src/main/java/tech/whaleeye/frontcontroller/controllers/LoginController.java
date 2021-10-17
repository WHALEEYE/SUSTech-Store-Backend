package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.whaleeye.frontcontroller.config.shiro.LoginToken;
import tech.whaleeye.frontcontroller.config.shiro.LoginType;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.Values;
import tech.whaleeye.misc.utils.JWTUtils;
import tech.whaleeye.model.entity.VCodeRecord;
import tech.whaleeye.service.StoreUserService;
import tech.whaleeye.service.VCodeRecordService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@RestController
@ApiOperation("Login Controller")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    StoreUserService storeUserService;

    @Autowired
    VCodeRecordService vCodeRecordService;

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
        LoginToken token = new LoginToken(phoneNumber, VCode, LoginType.PHONE_CODE);
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
        VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailLoginVCode(phoneNumber);
        vCodeRecordService.setVCodeUsed(vCodeRecord.getId());

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
        LoginToken token = new LoginToken(cardNumber, password, LoginType.CARD_PWD);
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
}
