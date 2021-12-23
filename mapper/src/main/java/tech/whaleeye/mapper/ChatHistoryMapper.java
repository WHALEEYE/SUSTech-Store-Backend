package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.vo.ChatHistory.ChatHistoryVO;

import java.util.Date;
import java.util.List;

@Mapper
public interface ChatHistoryMapper {

    List<ChatHistoryVO> listChatHistory(@Param("currentUser") Integer currentUser, @Param("otherUser") Integer otherUser, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    Integer addChatHistory(@Param("sender") Integer sender, @Param("receiver") Integer receiver, @Param("messageContent") String messageContent);

}
