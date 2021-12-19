package tech.whaleeye.service;

import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.entity.BackUser;
import tech.whaleeye.model.entity.BackUserRole;
import tech.whaleeye.model.vo.StoreUser.BackUserVO;

public interface BackUserService {

    BackUser queryById(Integer userId);

    BackUser queryByUsername(String userName);

    PageList<BackUserVO> listAllBackUsers(Integer pageSize, Integer pageNo);

    BackUserRole getRoleByUserId(Integer userId);

    Integer addNewBackUser(String username, String password, Integer roleId);

    Boolean updatePassword(Integer userId, String password);

    Boolean banUser(Integer userId);

    Boolean unbanUser(Integer userId);

    Boolean deleteBackUser(Integer userId, Integer deleteUserId);

}
