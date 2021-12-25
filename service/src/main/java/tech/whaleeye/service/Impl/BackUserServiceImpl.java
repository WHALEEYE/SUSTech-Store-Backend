package tech.whaleeye.service.Impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.BackUserMapper;
import tech.whaleeye.mapper.BackUserRoleMapper;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.misc.exceptions.IllegalPasswordException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.BackUser;
import tech.whaleeye.model.entity.BackUserRole;
import tech.whaleeye.model.vo.BackUser.BackUserVO;
import tech.whaleeye.service.BackUserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackUserServiceImpl implements BackUserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BackUserMapper backUserMapper;

    @Autowired
    private BackUserRoleMapper backUserRoleMapper;

    @Override
    public BackUser queryById(Integer userId) {
        return backUserMapper.queryById(userId);
    }

    @Override
    public BackUser queryByUsername(String userName) {
        return backUserMapper.queryByUsername(userName);
    }

    @Override
    public PageList<BackUserVO> listAllBackUsers(Integer pageSize, Integer pageNo, String searchKeyword) {
        List<BackUser> userList = backUserMapper.listAllBackUsers(pageSize, (pageNo - 1) * pageSize, searchKeyword);
        List<BackUserVO> userVOList = new ArrayList<>();
        BackUserVO backUserVO;
        for (BackUser backUser : userList) {
            backUserVO = modelMapper.map(backUser, BackUserVO.class);
            backUserVO.setRoleName(backUserRoleMapper.queryById(backUser.getRoleId()).getRoleName());
            userVOList.add(backUserVO);
        }
        int total = backUserMapper.countBackUsers();
        return new PageList<>(userVOList, pageSize, pageNo, total);
    }

    @Override
    public BackUserRole getRoleByUserId(Integer userId) {
        return backUserRoleMapper.queryById(backUserMapper.queryById(userId).getRoleId());
    }

    @Override
    public Integer addNewBackUser(String username, String password, Integer roleId) {
        if (!username.matches("[0-9a-zA-Z]*") || (roleId != 1 && roleId != 2)) {
            throw new InvalidValueException();
        } else if (!password.matches("[a-zA-Z0-9!@#$%^&*()_+\\-=,.<>?/\\\\|\\[\\]{}:;\"'`~]{6,20}")) {
            throw new IllegalPasswordException();
        }
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return backUserMapper.addNewBackUser(username, password, hexSalt, roleId);
    }

    @Override
    public Boolean updatePassword(Integer userId, String password) {
        if (!password.matches("[a-zA-Z0-9!@#$%^&*()_+\\-=,.<>?/\\\\|\\[\\]{}:;\"'`~]{6,20}")) {
            throw new IllegalPasswordException();
        }
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return backUserMapper.updatePassword(userId, password, hexSalt) > 0;
    }

    @Override
    public Boolean banUser(Integer userId) {
        return backUserMapper.banUser(userId) > 0;
    }

    @Override
    public Boolean unbanUser(Integer userId) {
        return backUserMapper.unbanUser(userId) > 0;
    }

    @Override
    public Boolean deleteBackUser(Integer userId, Integer deleteUserId) {
        return backUserMapper.deleteBackUser(userId, deleteUserId);
    }
}
