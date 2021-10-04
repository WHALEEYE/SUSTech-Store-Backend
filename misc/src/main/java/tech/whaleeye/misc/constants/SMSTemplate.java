package tech.whaleeye.misc.constants;

import lombok.Getter;

@Getter
public enum SMSTemplate {
    LOGIN_V_CODE("1148076"),
    CHANGE_PASSWORD_V_CODE("1148075"),
    CHANGE_ALIPAY_V_CODE("1148077"),
    CANCEL_ACCOUNT_V_CODE("1148078");

    private final String templateId;

    SMSTemplate(String templateId) {
        this.templateId = templateId;
    }
}
