package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.VCodeRecord;
import tech.whaleeye.service.StoreUserService;
import tech.whaleeye.service.VCodeRecordService;


@RestController
@Api("Controls Store User Accounts and Relationships")
@RequestMapping("/storeUser")
public class StoreUserController {

    @Autowired
    StoreUserService storeUserService;

    @Autowired
    VCodeRecordService vCodeRecordService;

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
        VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailAccountVCode(MiscUtils.currentUserId(), VCodeType.CANCEL_ACCOUNT);
        if (vCodeRecord == null || !vCodeRecord.getVCode().equals(vCode)) {
            return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired.");
        }
        vCodeRecordService.setVCodeUsed(vCodeRecord.getId());
        storeUserService.deleteStoreUser(MiscUtils.currentUserId(), MiscUtils.currentUserId());
        return AjaxResult.setSuccess(true).setMsg("Account has cancelled successfully.");
    }
}
