package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.CreditEvent;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CreditEventMapper {

    CreditEvent queryById(@Param("eventId") Integer eventId);

    List<CreditEvent> listAll();

    Integer updateCreditChange(@Param("eventId") Integer eventId, @Param("creditChange") BigDecimal creditChange);

}