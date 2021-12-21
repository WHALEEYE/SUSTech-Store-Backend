package tech.whaleeye.model.vo.ChatHistory;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ChatHistoryVO implements Serializable {

    private String messageContent;

    private Date sendTime;

    private Boolean isSender;

}
