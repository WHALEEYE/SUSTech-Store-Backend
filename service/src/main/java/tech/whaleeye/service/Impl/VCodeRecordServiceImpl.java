package tech.whaleeye.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.VCodeRecordMapper;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.constants.Values;
import tech.whaleeye.misc.exceptions.VCodeLimitException;
import tech.whaleeye.model.entity.VCodeRecord;
import tech.whaleeye.service.VCodeRecordService;

import java.util.Calendar;

@Service
public class VCodeRecordServiceImpl implements VCodeRecordService {

    @Autowired
    private VCodeRecordMapper vCodeRecordMapper;

    @Override
    public VCodeRecord getLatestAvailLoginVCode(String phoneNumber) {
        return vCodeRecordMapper.getLatest(null, phoneNumber, null, VCodeType.LOGIN.getTypeCode(), true);
    }

    @Override
    public VCodeRecord getLatestAvailAccountVCode(Integer userId, VCodeType vCodeType) {
        return vCodeRecordMapper.getLatest(userId, null, null, vCodeType.getTypeCode(), true);
    }

    @Override
    public VCodeRecord getLatestAvailEmailVCode(Integer userId, String cardNumber) {
        return vCodeRecordMapper.getLatest(userId, null, cardNumber, VCodeType.EMAIL_VERIFICATION.getTypeCode(), true);
    }

    @Override
    public Boolean setLoginVCode(String phoneNumber, String vCode) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, Values.V_CODE_EXPIRE_TIME_MINUTES - 1);
        VCodeRecord vCodeRecord = vCodeRecordMapper.getLatest(null, phoneNumber, null, VCodeType.LOGIN.getTypeCode(), false);
        if (vCodeRecord != null && vCodeRecord.getExpireTime().after(cal.getTime())) {
            // The interval of two sending requests are smaller than 1 minute
            throw new VCodeLimitException();
        }
        cal.add(Calendar.MINUTE, 1);
        return vCodeRecordMapper.insertVCodeRecord(null, phoneNumber, null, vCode, cal.getTime(), VCodeType.LOGIN.getTypeCode()) > 0;
    }

    @Override
    public Boolean setAccountVCode(Integer userId, String vCode, VCodeType vCodeType) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, Values.V_CODE_EXPIRE_TIME_MINUTES - 1);
        VCodeRecord vCodeRecord = vCodeRecordMapper.getLatest(userId, null, null, vCodeType.getTypeCode(), false);
        if (vCodeRecord != null && vCodeRecord.getExpireTime().after(cal.getTime())) {
            // The interval of two sending requests are smaller than 1 minute
            throw new VCodeLimitException();
        }
        cal.add(Calendar.MINUTE, 1);
        return vCodeRecordMapper.insertVCodeRecord(userId, null, null, vCode, cal.getTime(), vCodeType.getTypeCode()) > 0;
    }

    @Override
    public Boolean setEmailVCode(Integer userId, String cardNumber, String vCode) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, Values.V_CODE_EXPIRE_TIME_MINUTES - 1);
        VCodeRecord vCodeRecord = vCodeRecordMapper.getLatest(userId, null, cardNumber, VCodeType.EMAIL_VERIFICATION.getTypeCode(), false);
        if (vCodeRecord != null && vCodeRecord.getExpireTime().after(cal.getTime())) {
            // The interval of two sending requests are smaller than 1 minute
            throw new VCodeLimitException();
        }
        cal.add(Calendar.MINUTE, 1);
        return vCodeRecordMapper.insertVCodeRecord(userId, null, cardNumber, vCode, cal.getTime(), VCodeType.EMAIL_VERIFICATION.getTypeCode()) > 0;
    }

    @Override
    public void setVCodeUsed(Integer vCodeId) {
        vCodeRecordMapper.setVCodeUsed(vCodeId);
    }
}
