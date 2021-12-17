package tech.whaleeye.service;

import tech.whaleeye.model.entity.BackUserRole;

public interface BackUserRoleService {

    BackUserRole getRoleByUserId(Integer userId);

    BackUserRole getRoleByUsername(String username);

}
