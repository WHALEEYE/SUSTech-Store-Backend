package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.StoreUserVO;
import tech.whaleeye.service.StoreUserService;

@Api("Controllers For Test")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StoreUserService storeUserService;

    @ApiOperation("get user by id")
    @GetMapping("/getById/{id}")
    public AjaxResult getById(@PathVariable("id") Integer userId) {
        StoreUser storeUser = storeUserService.getStoreUserById(userId);
        if (storeUser == null) {
            return AjaxResult.setSuccess(false).setMsg("No corresponding user found!");
        } else {
            return AjaxResult.setSuccess(true).setData(modelMapper.map(storeUser, StoreUserVO.class));
        }
    }
}
