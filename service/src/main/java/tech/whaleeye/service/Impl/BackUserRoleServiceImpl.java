package tech.whaleeye.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.BackUserMapper;
import tech.whaleeye.mapper.BackUserRoleMapper;
import tech.whaleeye.model.entity.BackUserRole;
import tech.whaleeye.service.BackUserRoleService;

@Service
public class BackUserRoleServiceImpl implements BackUserRoleService {

    @Autowired
    private BackUserMapper backUserMapper;

    @Autowired
    private BackUserRoleMapper backUserRoleMapper;

    @Override
    public BackUserRole getRoleByUserId(Integer userId) {
        return backUserRoleMapper.queryById(backUserMapper.queryById(userId).getRoleId());
    }

    @Override
    public BackUserRole getRoleByUsername(String username) {
        return backUserRoleMapper.queryById(backUserMapper.queryByUsername(username).getRoleId());
    }
}
