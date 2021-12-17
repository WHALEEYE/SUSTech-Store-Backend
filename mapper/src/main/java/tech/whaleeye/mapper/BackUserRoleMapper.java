package tech.whaleeye.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tech.whaleeye.model.entity.BackUserRole;

import java.util.List;

@Mapper
public interface BackUserRoleMapper {

    BackUserRole queryById(@Param("roleId") Integer roleId);

    List<BackUserRole> listAllRoles();

}