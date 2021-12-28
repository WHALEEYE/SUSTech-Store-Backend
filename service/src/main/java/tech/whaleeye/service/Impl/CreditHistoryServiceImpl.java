package tech.whaleeye.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.CreditHistoryMapper;
import tech.whaleeye.service.CreditHistoryService;

@Service
public class CreditHistoryServiceImpl implements CreditHistoryService {

    @Autowired
    private CreditHistoryMapper creditHistoryMapper;

    @Override
    public void changeCredit(Integer userId, Integer eventId) {
        creditHistoryMapper.changeCredit(userId, eventId);
    }

}