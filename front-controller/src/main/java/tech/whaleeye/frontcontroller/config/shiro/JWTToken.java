package tech.whaleeye.frontcontroller.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import tech.whaleeye.misc.utils.JWTUtils;

public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    // JWT token
    private final String token;

    private final String userId;

    public JWTToken(String token) {
        this.token = token;
        String userId = JWTUtils.getClaimField(token, "userId");
        // there will be extra double quotes due to some bug in the JWT dependency, so we need to remove them
        this.userId = userId == null ? null : userId.substring(1, userId.length() - 1);
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}