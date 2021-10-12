package tech.whaleeye.frontcontroller.config.shiro;

import lombok.Getter;
import org.apache.shiro.authc.UsernamePasswordToken;

@Getter
public class LoginToken extends UsernamePasswordToken {
    private final LoginType loginType;

    public LoginToken(String account, String password, LoginType loginType) {
        super(account, password);
        this.loginType = loginType;
    }

}
