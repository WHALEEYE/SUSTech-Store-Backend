package tech.whaleeye.service;

import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.model.entity.BackUser;
import tech.whaleeye.model.vo.BackUserVO;

public interface BackUserService {

    BackUser queryById(Integer userId);

    BackUser queryByUsername(String userName);

    ListPage<BackUserVO> listAllBackUsers(Integer pageSize, Integer pageNo);

    Integer addNewBackUser(String username, String password, Integer roleId);

    Boolean updatePassword(Integer userId, String password);

    Boolean banUser(Integer userId);

    Boolean unbanUser(Integer userId);

}
