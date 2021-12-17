package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.service.BackUserService;

@Api("Background User Controller")
@RestController
@RequestMapping("/backUser")
@Log4j2
public class BackUserController {

    @Autowired
    private BackUserService backUserService;

    @ApiOperation("list all background users")
    @GetMapping("/all")
    @RequiresRoles("Super")
    public AjaxResult listAllBackUsers(Integer pageSize, Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(backUserService.listAllBackUsers(pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list users");
        }
    }

    @ApiOperation("create new background user")
    @PostMapping("/new")
    @RequiresRoles("Super")
    public AjaxResult createNewBackUser(String username, String password, Integer roleId) {
        try {
            return AjaxResult.setSuccess(true).setData(backUserService.addNewBackUser(username, password, roleId));
        } catch (InvalidValueException ive) {
            return AjaxResult.setSuccess(false).setMsg("Bad username or role");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to create background user");
        }
    }

    @ApiOperation("set user password")
    @PutMapping("/password/current")
    public AjaxResult updateCurrentPassword(String password) {
        try {
            if (backUserService.updatePassword(MiscUtils.currentUserId(), password) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
            }
            return AjaxResult.setSuccess(true).setMsg("Success.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
        }
    }

    @ApiOperation("update other users' passwords")
    @PutMapping("/password/{userId}")
    @RequiresRoles("Super")
    public AjaxResult updateOtherPassword(@PathVariable("userId") Integer userId, String password) {
        try {
            if (backUserService.updatePassword(userId, password) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
            }
            return AjaxResult.setSuccess(true).setMsg("Success.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
        }
    }

    @ApiOperation("ban background user")
    @PutMapping("ban/{userId}")
    @RequiresRoles("Super")
    public AjaxResult banUser(@PathVariable("userId") Integer userId) {
        try {
            if (backUserService.banUser(userId) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to ban user.");
            }
            return AjaxResult.setSuccess(true).setMsg("Success.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to ban user.");
        }
    }

}
