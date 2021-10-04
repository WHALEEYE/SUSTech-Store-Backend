package tech.whaleeye.misc.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import tech.whaleeye.misc.constants.Values;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author whaleeye
 **/
public class JWTUtils {

    /**
     * Verify the correctness of the token.
     *
     * @param token  the token
     * @param userId the ID of the current user
     * @param secret the secret
     * @return the token is correct or not
     */
    public static boolean verify(String token, String userId, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("userId", userId).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    /**
     * Get the customized field from token. No need of secret.
     *
     * @param token the token
     * @param field the name of the field
     * @return the value of the field if the field exists, else return null
     */
    public static String getClaimField(String token, String field) {
        try {
            return JWT.decode(token).getClaim(field).toString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * Generate the signature.
     *
     * @param userId the ID of the current user
     * @param secret the secret
     * @return the signature
     */
    public static String sign(String userId, String secret) {
        try {
            Date date = new Date(System.currentTimeMillis() + Values.JWT_EXPIRE_TIME_SECOND);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // Add information of user id into the signature
            return JWT.create().withClaim("userId", userId).withExpiresAt(date).sign(algorithm);
        } catch (JWTCreationException e) {
            return null;
        }
    }

    /**
     * Get the issue time of token.
     *
     * @param token the token
     * @return a {@link Date} object corresponding the issue time.
     */
    public static Date getIssuedAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getIssuedAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * Judge if the token has expired.
     *
     * @param token the token
     * @return expired or not
     */
    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }

    /**
     * Refresh the token. Need a secret to sign the token.
     *
     * @param token  the old token. The information in old token will be copied into new token.
     * @param secret the secret string.
     * @return the new token.
     */
    public static String refreshTokenExpired(String token, String secret) {
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        try {
            Date date = new Date(System.currentTimeMillis() + Values.JWT_EXPIRE_TIME_SECOND);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Builder builder = JWT.create().withExpiresAt(date);
            for (Entry<String, Claim> entry : claims.entrySet()) {
                builder.withClaim(entry.getKey(), entry.getValue().asString());
            }
            return builder.sign(algorithm);
        } catch (JWTCreationException e) {
            return null;
        }
    }
}