package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.VCodeType;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.VCodeRecord;
import tech.whaleeye.service.StoreUserService;
import tech.whaleeye.service.VCodeRecordService;


@Api("Controls Store User Accounts and Relationships")
@RestController
@RequestMapping("/storeUser")
@Log4j2
public class StoreUserController {

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private VCodeRecordService vCodeRecordService;

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
        try {
            VCodeRecord vCodeRecord = vCodeRecordService.getLatestAvailAccountVCode(MiscUtils.currentUserId(), VCodeType.CANCEL_ACCOUNT);
            if (vCodeRecord == null || !vCodeRecord.getVCode().equals(vCode)) {
                return AjaxResult.setSuccess(false).setMsg("Verification code incorrect or expired");
            }
            vCodeRecordService.setVCodeUsed(vCodeRecord.getId());
            if (storeUserService.deleteStoreUser(MiscUtils.currentUserId())) {
                return AjaxResult.setSuccess(true).setMsg("Account has cancelled successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to cancel account");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to cancel account");
        }
    }
}
