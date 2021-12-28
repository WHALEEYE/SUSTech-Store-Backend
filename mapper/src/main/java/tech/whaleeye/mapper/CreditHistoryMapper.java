package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.CreditHistory;

import java.util.List;

@Mapper
public interface CreditHistoryMapper {

    List<CreditHistory> listByUser(@Param("userId") Integer userId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer countByUser(@Param("userId") Integer userId);

    void changeCredit(@Param("userId") Integer userId, @Param("eventId") Integer eventId);

}