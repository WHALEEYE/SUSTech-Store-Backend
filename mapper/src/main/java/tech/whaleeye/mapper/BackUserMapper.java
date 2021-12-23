package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.BackUser;

import java.util.List;

@Mapper
public interface BackUserMapper {

    BackUser queryById(@Param("userId") Integer userId);

    BackUser queryByUsername(@Param("username") String username);

    List<BackUser> listAllBackUsers(@Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    Integer countBackUsers();

    Integer addNewBackUser(@Param("username") String username, @Param("password") String password, @Param("salt") String salt, @Param("roleId") Integer roleId);

    Integer updatePassword(@Param("userId") Integer userId, @Param("password") String password, @Param("salt") String salt);

    Integer banUser(@Param("userId") Integer userId);

    Integer unbanUser(@Param("userId") Integer userId);

    Boolean deleteBackUser(@Param("userId") Integer userId, @Param("deleteUserId") Integer deleteUserId);

}
