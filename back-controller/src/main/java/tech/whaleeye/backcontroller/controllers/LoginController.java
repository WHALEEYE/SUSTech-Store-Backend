package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.Values;
import tech.whaleeye.misc.utils.JWTUtils;
import tech.whaleeye.service.StoreUserService;
import tech.whaleeye.service.VCodeRecordService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

@Api("Login Controller")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    StoreUserService storeUserService;

    @Autowired
    VCodeRecordService vCodeRecordService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation("login by username and password")
    @PostMapping("/username")
    public AjaxResult cardLogin(ServletResponse response, String username, String password) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
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
