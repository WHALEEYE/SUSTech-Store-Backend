package tech.whaleeye.service;

import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.model.entity.VCodeRecord;

public interface VCodeRecordService {

    VCodeRecord getLatestLoginVCode(String phoneNumber);

    VCodeRecord getLatestAccountVCode(Integer userId, VCodeType vCodeType);

    VCodeRecord getLatestAvailLoginVCode(String phoneNumber);

    VCodeRecord getLatestAvailAccountVCode(Integer userId, VCodeType vCodeType);

    Integer setLoginVCode(String phoneNumber, String vCode);

    Integer setAccountVCode(Integer userId, String vCode, VCodeType vCodeType);

    Integer setMailVCode(Integer userId, String cardNumber, String vCode);

    void setVCodeUsed(Integer vCodeId);
}
