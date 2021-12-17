package tech.whaleeye.backcontroller.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import tech.whaleeye.model.entity.BackUser;
import tech.whaleeye.service.BackUserRoleService;
import tech.whaleeye.service.BackUserService;

/**
 * JwtRealm 只负责校验 JWTToken
 */
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    BackUserService backUserService;

    @Autowired
    BackUserRoleService backUserRoleService;

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
        Integer userId = Integer.parseInt((String) principalCollection.getPrimaryPrincipal());
        String roleName = backUserRoleService.getRoleByUserId(userId).getRoleName();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole(roleName);
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