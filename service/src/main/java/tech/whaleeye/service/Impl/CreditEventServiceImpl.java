package tech.whaleeye.service.Impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.CreditEventMapper;
import tech.whaleeye.model.vo.CreditSystem.CreditEventVO;
import tech.whaleeye.service.CreditEventService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CreditEventServiceImpl implements CreditEventService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CreditEventMapper creditEventMapper;

    @Override
    public List<CreditEventVO> listAll() {
        return modelMapper.map(creditEventMapper.listAll(), new TypeToken<List<CreditEventVO>>() {
        }.getType());
    }

    @Override
    public Boolean updateCreditChange(Integer eventId, BigDecimal creditChange) {
        return creditEventMapper.updateCreditChange(eventId, creditChange) > 0;
    }

}