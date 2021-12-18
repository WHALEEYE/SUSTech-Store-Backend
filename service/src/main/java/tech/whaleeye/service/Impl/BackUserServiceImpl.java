package tech.whaleeye.service.Impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.BackUserMapper;
import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.BackUser;
import tech.whaleeye.model.vo.BackUserVO;
import tech.whaleeye.service.BackUserService;

import java.util.List;

@Service
public class BackUserServiceImpl implements BackUserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BackUserMapper backUserMapper;

    @Override
    public BackUser queryById(Integer userId) {
        return backUserMapper.queryById(userId);
    }

    @Override
    public BackUser queryByUsername(String userName) {
        return backUserMapper.queryByUsername(userName);
    }

    @Override
    public ListPage<BackUserVO> listAllBackUsers(Integer pageSize, Integer pageNo) {
        List<BackUser> userList = backUserMapper.listAllBackUsers(pageSize, (pageNo - 1) * pageSize);
        List<BackUserVO> userVOList = modelMapper.map(userList, new TypeToken<List<BackUserVO>>() {
        }.getType());
        int total = backUserMapper.countBackUsers();
        return new ListPage<>(userVOList, pageSize, pageNo, total);
    }

    @Override
    public Integer addNewBackUser(String username, String password, Integer roleId) {
        if (!username.matches("[0-9a-zA-Z]*") || (roleId != 1 && roleId != 2)) {
            throw new InvalidValueException();
        }
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return backUserMapper.addNewBackUser(username, password, hexSalt, roleId);
    }

    @Override
    public Boolean updatePassword(Integer userId, String password) {
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
}
