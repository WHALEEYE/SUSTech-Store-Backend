package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.service.GoodTypeService;

@Api("good type controller")
@RestController
@RequestMapping("/goodType")
@Log4j2
public class GoodTypeController {

    @Autowired
    private GoodTypeService goodTypeService;

    @ApiOperation("list all good types")
    @GetMapping("/all")
    public AjaxResult listAllGoodTypes(@RequestParam Integer pageSize,
                                       @RequestParam Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(goodTypeService.listAllGoodTypes(pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list good types");
        }
    }

    @ApiOperation("create a new good type")
    @PostMapping("/")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    public AjaxResult createNewType(@RequestParam String typeName) {
        try {
            if (goodTypeService.createNewType(typeName)) {
                return AjaxResult.setSuccess(true).setMsg("New good type created successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed create new type");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed create new type");
        }
    }

    @ApiOperation("update type name")
    @PutMapping("/name/{typeId}")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    public AjaxResult updateTypeName(@PathVariable("typeId") Integer typeId,
                                     @RequestParam String typeName) {
        try {
            if (goodTypeService.updateTypeName(typeId, typeName)) {
                return AjaxResult.setSuccess(true).setMsg("Changed the name successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to change the name");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to change the name");
        }
    }

    @ApiOperation("move up one position")
    @PutMapping("/pos/up/{typeId}")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    public AjaxResult moveUp(@PathVariable("typeId") Integer typeId) {
        try {
            if (goodTypeService.moveUp(typeId)) {
                return AjaxResult.setSuccess(true).setMsg("Moved up successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed. It's already the first one.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to move up");
        }
    }

    @ApiOperation("move down one position")
    @PutMapping("/pos/down/{typeId}")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    public AjaxResult moveDown(@PathVariable("typeId") Integer typeId) {
        try {
            if (goodTypeService.moveDown(typeId)) {
                return AjaxResult.setSuccess(true).setMsg("Moved down successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed. It's already the last one.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to move down");
        }
    }

    @ApiOperation("delete one good type")
    @DeleteMapping("/{typeId}")
    @RequiresRoles(value = {"Admin", "Super"}, logical = Logical.OR)
    public AjaxResult deleteType(@PathVariable("typeId") Integer typeId) {
        try {
            if (goodTypeService.deleteType(typeId)) {
                return AjaxResult.setSuccess(true).setMsg("Good type deleted successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to delete the type");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to delete the type");
        }
    }

}
