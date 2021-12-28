package tech.whaleeye.service;

import tech.whaleeye.model.vo.CreditSystem.CreditEventVO;

import java.math.BigDecimal;
import java.util.List;

public interface CreditEventService {

    List<CreditEventVO> listAll();

    Boolean updateCreditChange(Integer eventId, BigDecimal creditChange);

}