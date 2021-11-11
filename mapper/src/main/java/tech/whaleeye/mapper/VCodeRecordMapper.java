package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import tech.whaleeye.model.entity.VCodeRecord;

import java.util.Date;

@Mapper
public interface VCodeRecordMapper {
    VCodeRecord getLatest(@Nullable @Param("userId") Integer userId,
                          @Nullable @Param("phoneNumber") String phoneNumber,
                          @Nullable @Param("cardNumber") String cardNumber,
                          @Param("type") Integer type,
                          @Param("availOnly") Boolean availOnly);

    Integer insertVCodeRecord(@Nullable @Param("userId") Integer userId,
                              @Nullable @Param("phoneNumber") String phoneNumber,
                              @Nullable @Param("cardNumber") String cardNumber,
                              @Param("vCode") String vCode,
                              @Param("expireTime") Date expireTime,
                              @Param("type") Integer type);

    void setVCodeUsed(@Param("vCodeId") Integer vCodeId);

}