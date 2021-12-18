package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.service.SecondHandGoodService;

@Api("second hand good controller")
@RestController
@RequestMapping("/good")
@Log4j2
public class SecondHandGoodController {

    @Autowired
    private SecondHandGoodService secondHandGoodService;

    @ApiOperation("list all the goods")
    @GetMapping("/all")
    public AjaxResult listAllGoodForBack(Integer pageSize,
                                         Integer pageNo,
                                         @RequestParam(required = false) String searchNickname,
                                         @RequestParam(required = false) String searchPhoneNumber,
                                         @RequestParam(required = false) String searchKeyword) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.listAllGoodsForBack(pageSize, pageNo, searchNickname, searchPhoneNumber, searchKeyword));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list goods");
        }
    }

    @ApiOperation("put a good off the shelf")
    @DeleteMapping("/{goodId}")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    AjaxResult offShelf(@PathVariable("goodId") Integer goodId) {
        try {
            if (secondHandGoodService.deleteGood(goodId, MiscUtils.currentUserId())) {
                return AjaxResult.setSuccess(true).setMsg("Good is put off the shelf successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to put the good off the shelf");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to put the good off the shelf");
        }
    }

}
