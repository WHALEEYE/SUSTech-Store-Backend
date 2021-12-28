package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.service.CreditEventService;

import java.math.BigDecimal;

@Api("Credit Event Controller")
@RestController
@RequestMapping("/credit")
@Log4j2
public class CreditEventController {

    @Autowired
    CreditEventService creditEventService;

    @ApiOperation("list all the credit events")
    @GetMapping("/all")
    AjaxResult listAllCreditEvents() {
        try {
            return AjaxResult.setSuccess(true).setData(creditEventService.listAll());
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list credit events");
        }
    }

    @ApiOperation("update credit change")
    @PatchMapping("/change/{eventId}")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    AjaxResult banUser(@PathVariable("eventId") Integer eventId,
                       @RequestParam BigDecimal creditChange) {
        try {
            if (creditEventService.updateCreditChange(eventId, creditChange)) {
                return AjaxResult.setSuccess(true).setMsg("Success.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to update");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to update");
        }
    }

}
