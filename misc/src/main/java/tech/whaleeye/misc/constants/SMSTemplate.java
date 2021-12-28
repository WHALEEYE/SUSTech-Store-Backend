package tech.whaleeye.misc.constants;

import lombok.Getter;

@Getter
public enum SMSTemplate {
    LOGIN_V_CODE("1148076"),
    CHANGE_PASSWORD_V_CODE("1148075"),
    CHANGE_ALIPAY_V_CODE("1148077"),
    CANCEL_ACCOUNT_V_CODE("1148078"),
    NEW_ORDER("1259030"),
    SELLER_ACK("1236233"),
    TRADE_SELLER("1227653"),
    TRADE_BUYER("1233248"),
    ORDER_CONFIRM("1236407"),
    ORDER_REFUND("1236406"),
    ORDER_CANCEL("1236405");

    private final String templateId;

    SMSTemplate(String templateId) {
        this.templateId = templateId;
    }
}
