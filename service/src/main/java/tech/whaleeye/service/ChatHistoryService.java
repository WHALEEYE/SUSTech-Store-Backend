package tech.whaleeye.service;

import tech.whaleeye.model.vo.ChatHistory.ChatHistoryVO;

import java.util.Date;
import java.util.List;

public interface ChatHistoryService {

    List<ChatHistoryVO> listChatHistory(Integer userId, Date beginTime, Date endTime);

    Boolean addChatHistory(Integer sender, Integer receiver, String messageContent);

}
