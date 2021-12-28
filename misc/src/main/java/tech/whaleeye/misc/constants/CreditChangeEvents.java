package tech.whaleeye.misc.constants;

import lombok.Getter;

@Getter
public enum CreditChangeEvents {

    LOGIN(1, "Login"),
    COMM(2, "Great Comment Received"),
    TRADE(3, "Trade Successfully"),
    WRONG(4, "Wrong Input");

    final Integer code;
    final String info;

    CreditChangeEvents(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

}
