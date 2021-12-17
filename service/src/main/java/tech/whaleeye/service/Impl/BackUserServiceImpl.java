package tech.whaleeye.service.Impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.BackUserMapper;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.BackUser;
import tech.whaleeye.service.BackUserService;

@Service
public class BackUserServiceImpl implements BackUserService {

    @Autowired
    BackUserMapper backUserMapper;

    @Override
    public BackUser queryById(Integer userId) {
        return backUserMapper.queryById(userId);
    }

    @Override
    public BackUser queryByUsername(String userName) {
        return backUserMapper.queryByUsername(userName);
    }

    @Override
    public Integer updatePassword(Integer userId, String password) {
        String hexSalt = MiscUtils.generateSalt(8);
        password = new Md5Hash(password, MiscUtils.getSaltFromHex(hexSalt), 1024).toHex();
        return backUserMapper.updatePassword(userId, password, hexSalt);
    }
}
