package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.service.StoreUserService;

@Api("controller for test only")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    StoreUserService storeUserService;

    @ApiOperation("set user password")
    @PutMapping("/password")
    public AjaxResult setPassword(String password) {
        if (storeUserService.setPassword(MiscUtils.currentUserId(), password) <= 0) {
            return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
        }
        return AjaxResult.setSuccess(true).setMsg("Success.");
    }
}
