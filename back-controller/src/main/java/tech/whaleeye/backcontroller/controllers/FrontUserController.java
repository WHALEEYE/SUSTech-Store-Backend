package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.service.StoreUserService;

@Api("Front User Controller")
@RestController
@RequestMapping("/frontUser")
@Log4j2
public class FrontUserController {

    @Autowired
    private StoreUserService storeUserService;

    @ApiOperation("list all the front users")
    @GetMapping("/all")
    public AjaxResult listAllFrontUsers(@RequestParam Integer pageSize,
                                        @RequestParam Integer pageNo,
                                        @RequestParam(required = false) String searchNickname,
                                        @RequestParam(required = false) String searchPhoneNumber) {
        try {
            return AjaxResult.setSuccess(true).setData(storeUserService.listAll(pageSize, pageNo, searchNickname, searchPhoneNumber));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list front users");
        }
    }

    @ApiOperation("Ban front user")
    @PutMapping("/ban/{userId}")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    public AjaxResult banUser(@PathVariable("userId") Integer userId) {
        try {
            if (storeUserService.banUser(userId)) {
                return AjaxResult.setSuccess(true).setMsg("Success.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to ban user.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to ban user.");
        }
    }

    @ApiOperation("unban front user")
    @PutMapping("/unban/{userId}")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    public AjaxResult unbanUser(@PathVariable("userId") Integer userId) {
        try {
            if (storeUserService.unbanUser(userId)) {
                return AjaxResult.setSuccess(true).setMsg("Success.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to unban user.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to unban user.");
        }
    }

}
