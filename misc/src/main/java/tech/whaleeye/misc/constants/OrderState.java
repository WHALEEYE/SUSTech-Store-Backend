package tech.whaleeye.misc.constants;

import lombok.Getter;

@Getter
public enum OrderState {
    ACK_PENDING(SMSTemplate.NEW_ORDER),
    PAY_PENDING(SMSTemplate.SELLER_ACK),
    TRADING(null),
    DEAL(SMSTemplate.ORDER_CONFIRM),
    REFUND(SMSTemplate.ORDER_REFUND),
    CANCELED(SMSTemplate.ORDER_CANCEL);

    private final SMSTemplate smsTemplate;

    OrderState(SMSTemplate smsTemplate) {
        this.smsTemplate = smsTemplate;
    }
}
