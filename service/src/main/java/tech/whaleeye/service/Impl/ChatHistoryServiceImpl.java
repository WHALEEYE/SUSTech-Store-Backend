package tech.whaleeye.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.ChatHistoryMapper;
import tech.whaleeye.mapper.StoreUserMapper;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.vo.ChatHistory.ChatHistoryVO;
import tech.whaleeye.model.vo.StoreUser.BriefUserVO;
import tech.whaleeye.service.ChatHistoryService;

import java.util.Date;
import java.util.List;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    @Autowired
    ChatHistoryMapper chatHistoryMapper;

    @Autowired
    StoreUserMapper storeUserMapper;

    @Override
    public List<ChatHistoryVO> listChatHistory(Integer currentUser, Integer otherUser, Date beginTime, Date endTime) {
        return chatHistoryMapper.listChatHistory(currentUser, otherUser, beginTime, endTime);
    }

    @Override
    public List<BriefUserVO> getFriendList() {
        return storeUserMapper.listFriends(MiscUtils.currentUserId());
    }

    @Override
    public Boolean addChatHistory(Integer sender, Integer receiver, String messageContent) {
        return chatHistoryMapper.addChatHistory(sender, receiver, messageContent) > 0;
    }

}
