package tech.whaleeye.service;

import tech.whaleeye.model.vo.ChatHistory.ChatHistoryVO;
import tech.whaleeye.model.vo.StoreUser.BriefUserVO;

import java.util.Date;
import java.util.List;

public interface ChatHistoryService {

    List<ChatHistoryVO> listChatHistory(Integer currentUser, Integer otherUser, Date beginTime, Date endTime);

    List<BriefUserVO> getFriendList();

    Boolean addChatHistory(Integer sender, Integer receiver, String messageContent);

}
