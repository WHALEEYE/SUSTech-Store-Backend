package tech.whaleeye.backcontroller.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.entity.VCodeRecord;
import tech.whaleeye.service.StoreUserService;
import tech.whaleeye.service.VCodeRecordService;

import java.util.Date;

public class PhoneVCodeRealm extends AuthorizingRealm {
    @Autowired
    StoreUserService storeUserService;

    @Autowired
    VCodeRecordService vCodeRecordService;

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
        VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailLoginVCode(loginToken.getUsername());
        if (storeUser == null) {
            throw new UnknownAccountException();
        } else if (storeUser.getBanned()) {
            throw new LockedAccountException();
        } else if (vCodeRecord == null || vCodeRecord.getUsedTime() != null || new Date().after(vCodeRecord.getExpireTime())) {
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(storeUser.getId(), vCodeRecord.getVCode(), getName());
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        if (authenticationToken instanceof UsernamePasswordToken) {
            LoginToken loginToken = (LoginToken) authenticationToken;
            return loginToken.getLoginType() == LoginType.PHONE_CODE;
        } else {
            return false;
        }
    }
}
