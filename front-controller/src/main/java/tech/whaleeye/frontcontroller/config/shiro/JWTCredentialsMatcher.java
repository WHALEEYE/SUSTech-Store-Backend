package tech.whaleeye.frontcontroller.config.shiro;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.whaleeye.misc.constants.Values;

public class JWTCredentialsMatcher implements CredentialsMatcher {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * In doCredentialMatch(), we only need token itself to verify, no need of authentication info.
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        String token = authenticationToken.getCredentials().toString();
        String userId = authenticationToken.getPrincipal().toString();
        try {
            Algorithm algorithm = Algorithm.HMAC256(Values.JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("userId", userId).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

}