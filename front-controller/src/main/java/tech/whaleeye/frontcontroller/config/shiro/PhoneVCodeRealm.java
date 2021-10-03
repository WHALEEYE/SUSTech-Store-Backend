package tech.whaleeye.frontcontroller.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.service.StoreUserService;

import java.util.Date;

public class PhoneVCodeRealm extends AuthorizingRealm {
    @Autowired
    StoreUserService storeUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("user");
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        LoginToken loginToken = (LoginToken) authenticationToken;
        StoreUser storeUser = storeUserService.getStoreUserByPhoneNumber(loginToken.getUsername());
        if (storeUser == null) {
            throw new UnknownAccountException();
        } else if (storeUser.getBanned()) {
            throw new LockedAccountException();
        } else if (storeUser.getVCodeExpireTime() == null || storeUser.getVCode() == null || new Date().after(storeUser.getVCodeExpireTime())) {
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(storeUser.getId(), storeUser.getVCode(), getName());
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        if (authenticationToken instanceof UsernamePasswordToken) {
            LoginToken loginToken = (LoginToken) authenticationToken;
            return loginToken.getLoginType() == LoginToken.LoginType.PHONE_CODE;
        } else {
            return false;
        }
    }
}
