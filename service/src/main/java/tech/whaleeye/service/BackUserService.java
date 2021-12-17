package tech.whaleeye.service;

import tech.whaleeye.model.entity.BackUser;

public interface BackUserService {

    BackUser queryById(Integer userId);

    BackUser queryByUsername(String userName);

    Integer updatePassword(Integer userId, String password);

}
