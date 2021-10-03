package tech.whaleeye.frontcontroller.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.service.StoreUserService;

/**
 * JwtRealm 只负责校验 JWTToken
 */
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    StoreUserService storeUserService;

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
        StoreUser storeUser = storeUserService.getStoreUserById(userId);

        if (storeUser == null) {
            throw new UnknownAccountException();
        } else if (storeUser.getBanned()) {
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(userId, userId, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("user");
        return authorizationInfo;
    }

//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        // 获取当前用户
//        StoreUser currentUser = (StoreUser) MiscUtils.getSubject().getPrincipal();
//        // UserEntity currentUser = (UserEntity) principals.getPrimaryPrincipal();
//        // 查询数据库，获取用户的角色信息
//        Set<String> roles = ShiroRealm.roleMap.get(currentUser.getName());
//        // 查询数据库，获取用户的权限信息
//        Set<String> perms = ShiroRealm.permMap.get(currentUser.getName());
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setRoles(roles);
//        info.setStringPermissions(perms);
//        return info;
//    }

}