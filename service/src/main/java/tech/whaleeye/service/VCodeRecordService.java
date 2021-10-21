package tech.whaleeye.service;

import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.model.entity.VCodeRecord;

public interface VCodeRecordService {

    VCodeRecord getLatestAvailLoginVCode(String phoneNumber);

    VCodeRecord getLatestAvailAccountVCode(Integer userId, VCodeType vCodeType);

    VCodeRecord getLatestAvailEmailVCode(Integer userId, String cardNumber);

    Integer setLoginVCode(String phoneNumber, String vCode);

    Integer setAccountVCode(Integer userId, String vCode, VCodeType vCodeType);

    Integer setEmailVCode(Integer userId, String cardNumber, String vCode);

    void setVCodeUsed(Integer vCodeId);
}
