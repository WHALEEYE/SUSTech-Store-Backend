package tech.whaleeye.misc.constants;

import lombok.Getter;

@Getter
public enum InformType {
    TRADE_ESTABLISHED(0, SMSTemplate.TRADE_ESTABLISHED_INFORM);

    private final int typeCode;
    private final SMSTemplate smsTemplate;

    InformType(int typeCode, SMSTemplate smsTemplate) {
        this.typeCode = typeCode;
        this.smsTemplate = smsTemplate;
    }
}
