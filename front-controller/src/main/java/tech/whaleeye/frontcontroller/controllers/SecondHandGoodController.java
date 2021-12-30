package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.constants.UploadFileType;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.exceptions.LowCreditException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.vo.GoodType.GoodTypeVO;
import tech.whaleeye.service.SecondHandGoodService;
import tech.whaleeye.service.StoreUserService;

import java.io.IOException;
import java.util.List;

@Api("Second Hand Good Controller")
@RestController
@RequestMapping("/good")
@Log4j2
public class SecondHandGoodController {

    @Autowired
    private SecondHandGoodService secondHandGoodService;

    @Autowired
    private StoreUserService storeUserService;

    @ApiOperation("get one good's full information")
    @GetMapping("/detail/{goodId}")
    AjaxResult getGoodById(@PathVariable("goodId") Integer goodId) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.getGoodById(goodId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to get the good's information.");
        }
    }

    @ApiOperation("get one good's brief information")
    @GetMapping("/brief/{goodId}")
    AjaxResult getBriefGoodById(@PathVariable("goodId") Integer goodId) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.getBriefGoodById(goodId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to get the good's information.");
        }
    }

    @ApiOperation("list all goods")
    @GetMapping("/all")
    AjaxResult listAllGoods(@RequestParam Integer pageSize,
                            @RequestParam Integer pageNo,
                            @RequestParam(required = false) Integer typeId,
                            @RequestParam(required = false) String searchKeyword) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.listAllGoods(pageSize, pageNo, typeId, searchKeyword));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list goods.");
        }
    }

    @RequiresRoles("user")
    @ApiOperation("list goods of the current user")
    @GetMapping("/current")
    AjaxResult listGoods(@RequestParam Integer pageSize,
                         @RequestParam Integer pageNo,
                         @RequestParam(required = false) Boolean sold,
                         @RequestParam(required = false) String searchKeyword) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.getGoodsByPublisher(MiscUtils.currentUserId(), pageSize, pageNo, sold, searchKeyword));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list user's good.");
        }
    }

    @ApiOperation("list goods of other user")
    @GetMapping("/all/{userId}")
    AjaxResult listGoods(@PathVariable("userId") Integer userId,
                         @RequestParam Integer pageSize,
                         @RequestParam Integer pageNo,
                         @RequestParam(required = false) Boolean sold,
                         @RequestParam(required = false) String searchKeyword) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.getGoodsByPublisher(userId, pageSize, pageNo, sold, searchKeyword));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list user's good.");
        }
    }

    @ApiOperation("get all the good types")
    @GetMapping("/type/all")
    AjaxResult getAllGoodTypes() {
        try {
            List<GoodTypeVO> goodTypeVOList = secondHandGoodService.getAllGoodTypes();
            return AjaxResult.setSuccess(true).setData(goodTypeVOList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to get good types");
        }
    }

    @ApiOperation("get a good type by id")
    @GetMapping("/type/{typeId}")
    AjaxResult getGoodTypeById(@PathVariable("typeId") Integer typeId) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.getGoodTypeById(typeId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to get good type");
        }
    }

    @ApiOperation("list the collectors of one good")
    @GetMapping("/collectors/{goodId}")
    AjaxResult getAllCollectors(@PathVariable("goodId") Integer goodId,
                                @RequestParam Integer pageSize,
                                @RequestParam Integer pageNo) {
        try {
            return AjaxResult.setSuccess(true).setData(storeUserService.listCollectors(goodId, pageSize, pageNo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed list collectors");
        }
    }

    @ApiOperation("create a new good")
    @PostMapping("/new")
    AjaxResult createNewGood(@RequestBody SecondHandGoodDTO secondHandGoodDTO) {
        try {
            if (secondHandGoodService.insertSecondHandGood(secondHandGoodDTO)) {
                return AjaxResult.setSuccess(true).setMsg("Good created successfully.");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to create new good.");
        } catch (LowCreditException lce) {
            return AjaxResult.setSuccess(false).setMsg("Your credit is too low or no card bounded");
        } catch (InvalidValueException ive) {
            return AjaxResult.setSuccess(false).setMsg("Invalid good price.");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to create new good.");
        }
    }

    @ApiOperation("upload good pictures")
    @PostMapping("/picture")
    AjaxResult uploadPicture(@RequestPart MultipartFile picture) {
        try {
            return AjaxResult.setSuccess(true).setData(MiscUtils.processPicture(picture, UploadFileType.DESC_PIC));
        } catch (IOException e) {
            log.error("User ID [" + MiscUtils.currentUserId() + "]: failed to upload picture.");
            return AjaxResult.setSuccess(false).setMsg("Failed to upload picture.");
        } catch (InvalidValueException e) {
            return AjaxResult.setSuccess(false).setMsg("Invalid picture. Seems like it's not a picture.");
        }
    }

    @ApiOperation("collect one good")
    @PostMapping("/collect/{goodId}")
    AjaxResult collectGood(@PathVariable("goodId") Integer goodId) {
        try {
            if (secondHandGoodService.collectGood(goodId)) {
                return AjaxResult.setSuccess(true).setMsg("Collected successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to add collection");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to add collection");
        }
    }

    @ApiOperation("cancel collect one good")
    @DeleteMapping("/cancelCollect/{goodId}")
    AjaxResult cancelCollectGood(@PathVariable("goodId") Integer goodId) {
        try {
            if (secondHandGoodService.cancelCollectGood(goodId)) {
                return AjaxResult.setSuccess(true).setMsg("Collection deleted successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to cancel collection");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to cancel collection");
        }
    }

    @ApiOperation("update good information")
    @PutMapping("/info")
    AjaxResult updateGoodInfo(@RequestBody SecondHandGoodDTO secondHandGoodDTO) {
        try {
            if (secondHandGoodService.updateGoodInfo(secondHandGoodDTO)) {
                return AjaxResult.setSuccess(true).setMsg("Good information updated successfully.");
            }
            return AjaxResult.setSuccess(false).setMsg("Good information failed to update.");
        } catch (InvalidValueException ive) {
            return AjaxResult.setSuccess(false).setMsg("Invalid good price.");
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("You are not allowed to do this operation");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Good information failed to update.");
        }
    }

    @ApiOperation("put a good off the shelf")
    @DeleteMapping("/{goodId}")
    AjaxResult offShelf(@PathVariable("goodId") Integer goodId) {
        try {
            if (secondHandGoodService.deleteGood(goodId, MiscUtils.currentUserId())) {
                return AjaxResult.setSuccess(true).setMsg("Good is put off the shelf successfully");
            }
            return AjaxResult.setSuccess(false).setMsg("Failed to put the good off the shelf");
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("You are not allowed to put this good off the shelf");
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to put the good off the shelf");
        }
    }
}
