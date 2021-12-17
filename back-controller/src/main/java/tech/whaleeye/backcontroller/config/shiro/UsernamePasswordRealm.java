package tech.whaleeye.backcontroller.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.BackUser;
import tech.whaleeye.service.BackUserRoleService;
import tech.whaleeye.service.BackUserService;

public class UsernamePasswordRealm extends AuthorizingRealm {

    @Autowired
    BackUserService backUserService;

    @Autowired
    BackUserRoleService backUserRoleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Integer userId = (Integer) principalCollection.getPrimaryPrincipal();
        String roleName = backUserRoleService.getRoleByUserId(userId).getRoleName();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole(roleName);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken loginToken = (UsernamePasswordToken) authenticationToken;
        BackUser backUser = backUserService.queryByUsername(loginToken.getUsername());
        if (backUser == null) {
            throw new UnknownAccountException();
        } else if (backUser.getBanned()) {
            throw new LockedAccountException();
        }
        return new SimpleAuthenticationInfo(backUser.getId(), backUser.getPassword(), MiscUtils.getSaltFromHex(backUser.getSalt()), getName());
    }

}
