package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.service.StoreUserService;

import java.util.Date;


@RestController
@Api("Controls Store User Accounts and Relationships")
@RequestMapping("/storeUser")
public class StoreUserController {

    @Autowired
    StoreUserService storeUserService;

    @Autowired
    ModelMapper modelMapper;

    @ApiOperation("Follow One User")
    @PostMapping("/follow/{followedId}")
    public AjaxResult followUser(@PathVariable("followedId") Integer followedId) {
        try {
            if (storeUserService.followUser(MiscUtils.currentUserId(), followedId) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to Follow");
            }
            return AjaxResult.setSuccess(true).setMsg("Followed Successfully");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to Follow");
        }
    }

    @ApiOperation("cancel user account")
    @DeleteMapping("/cancel")
    public AjaxResult cancelAccount(String vCode) {
        StoreUser storeUser = storeUserService.getStoreUserById(MiscUtils.currentUserId());
        if (storeUser.getVCodeExpireTime() == null
                || storeUser.getVCode() == null
                || storeUser.getVCodeType() != VCodeType.CANCEL_ACCOUNT.getTypeCode()
                || new Date().after(storeUser.getVCodeExpireTime())
                || !storeUser.getVCode().equals(vCode)) {
            return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired.");
        }
        storeUserService.clearVCode(storeUser.getPhoneNumber());
        storeUserService.deleteStoreUser(MiscUtils.currentUserId(), MiscUtils.currentUserId());
        return AjaxResult.setSuccess(true).setMsg("Account has cancelled successfully.");
    }
}
