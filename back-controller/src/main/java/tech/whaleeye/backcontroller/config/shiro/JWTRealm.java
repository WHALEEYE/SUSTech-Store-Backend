package tech.whaleeye.backcontroller.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import tech.whaleeye.model.entity.BackUser;
import tech.whaleeye.service.BackUserService;

/**
 * JwtRealm 只负责校验 JWTToken
 */
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    BackUserService backUserService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * There is no need of info in {@link JWTCredentialsMatcher}, so we can return anything.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authenticationToken;
        if (jwtToken.getPrincipal() == null) {
            throw new AccountException("Invalid Token");
        }

        // get the current user from token
        Integer userId = Integer.parseInt((String) jwtToken.getPrincipal());
        BackUser backUser = backUserService.queryById(userId);

        if (backUser == null) {
            throw new UnknownAccountException();
        } else if (backUser.getBanned()) {
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(userId, userId, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Integer userId = (Integer) principalCollection.getPrimaryPrincipal();
        String roleName = backUserService.getRoleByUserId(userId).getRoleName();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole(roleName);
        return authorizationInfo;
    }

}