package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.BackUser;

@Mapper
public interface BackUserMapper {

    BackUser queryById(@Param("userId") Integer userId);

    BackUser queryByUsername(@Param("username") String username);

    BackUser listAllBackUsers(@Param("roleId") Integer roleId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer updatePassword(@Param("userId") Integer userId, @Param("password") String password, @Param("salt") String salt);

}
