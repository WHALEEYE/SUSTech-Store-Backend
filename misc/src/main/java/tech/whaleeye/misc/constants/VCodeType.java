package tech.whaleeye.misc.constants;

import lombok.Getter;

// verification code type
@Getter
public enum VCodeType {
    LOGIN(0, SMSTemplate.LOGIN_V_CODE),
    CHANGE_PASSWORD(1, SMSTemplate.CHANGE_PASSWORD_V_CODE),
    CHANGE_ALIPAY(2, SMSTemplate.CHANGE_ALIPAY_V_CODE),
    CANCEL_ACCOUNT(3, SMSTemplate.CANCEL_ACCOUNT_V_CODE),
    MAIL_VERIFICATION(4, null);

    private final int typeCode;
    private final SMSTemplate smsTemplate;

    VCodeType(int typeCode, SMSTemplate smsTemplate) {
        this.typeCode = typeCode;
        this.smsTemplate = smsTemplate;
    }

}