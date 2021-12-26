package tech.whaleeye.service;

import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.model.entity.VCodeRecord;

public interface VCodeRecordService {

    VCodeRecord getLatestAvailLoginVCode(String phoneNumber);

    VCodeRecord getLatestAvailAccountVCode(Integer userId, VCodeType vCodeType);

    VCodeRecord getLatestAvailEmailVCode(Integer userId, String cardNumber);

    Boolean setLoginVCode(String phoneNumber, String vCode);

    Boolean setAccountVCode(Integer userId, String vCode, VCodeType vCodeType);

    Boolean setEmailVCode(Integer userId, String cardNumber, String vCode);

    void setVCodeUsed(Integer vCodeId);
}
