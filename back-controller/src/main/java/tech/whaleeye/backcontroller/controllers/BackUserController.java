package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.exceptions.DuplicateException;
import tech.whaleeye.misc.exceptions.IllegalPasswordException;
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

    @ApiOperation("get information of the current user")
    @GetMapping("/info")
    AjaxResult getCurrentInfo() {
        try {
            return AjaxResult.setSuccess(true).setData(backUserService.queryById(MiscUtils.currentUserId()));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list users");
        }
    }


    @ApiOperation("list all background users")
    @GetMapping("/all")
    @RequiresRoles("Super")
    AjaxResult listAllBackUsers(@RequestParam Integer pageSize,
                                @RequestParam Integer pageNo,
                                @RequestParam(required = false) String searchKeyword) {
        try {
            return AjaxResult.setSuccess(true).setData(backUserService.listAllBackUsers(pageSize, pageNo, searchKeyword));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list users");
        }
    }

    @ApiOperation("create new background user")
    @PostMapping("/")
    @RequiresRoles("Super")
    AjaxResult createNewBackUser(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam Integer roleId) {
        try {
            return AjaxResult.setSuccess(true).setData(backUserService.addNewBackUser(username, password, roleId));
        } catch (IllegalPasswordException ipe) {
            return AjaxResult.setSuccess(false).setMsg("This password is illegal");
        } catch (InvalidValueException ive) {
            return AjaxResult.setSuccess(false).setMsg("Bad username or role");
        } catch (DuplicateException de) {
            return AjaxResult.setSuccess(false).setMsg("The username already exists");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to create background user");
        }
    }

    @ApiOperation("update user password")
    @PatchMapping("/password")
    AjaxResult updateCurrentPassword(@RequestParam String password) {
        try {
            if (backUserService.updatePassword(MiscUtils.currentUserId(), password)) {
                return AjaxResult.setSuccess(true).setMsg("Success.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
        } catch (IllegalPasswordException ipe) {
            return AjaxResult.setSuccess(false).setMsg("This password is illegal");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to set password.");
        }
    }

    @ApiOperation("ban background user")
    @PatchMapping("/ban/{userId}")
    @RequiresRoles("Super")
    AjaxResult banUser(@PathVariable("userId") Integer userId) {
        try {
            if (backUserService.banUser(userId)) {
                return AjaxResult.setSuccess(true).setMsg("Success.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to ban user.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to ban user.");
        }
    }

    @ApiOperation("unban background user")
    @PatchMapping("/unban/{userId}")
    @RequiresRoles("Super")
    AjaxResult unbanUser(@PathVariable("userId") Integer userId) {
        try {
            if (backUserService.unbanUser(userId)) {
                return AjaxResult.setSuccess(true).setMsg("Success.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to unban user.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to unban user.");
        }
    }

    @ApiOperation("delete background user")
    @DeleteMapping("/{userId}")
    @RequiresRoles("Super")
    AjaxResult deleteBackUser(@PathVariable("userId") Integer userId) {
        try {
            if (backUserService.deleteBackUser(userId, MiscUtils.currentUserId())) {
                return AjaxResult.setSuccess(true).setMsg("Success.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to delete user.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to delete user.");
        }
    }

}
