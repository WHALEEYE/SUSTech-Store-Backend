package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.misc.constants.UploadFileType;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.dto.SecondHandGoodDTO;
import tech.whaleeye.model.vo.BriefGoodVO;
import tech.whaleeye.model.vo.GoodTypeVO;
import tech.whaleeye.service.SecondHandGoodService;

import java.io.IOException;
import java.util.List;

@Api("Second Hand Good Controller")
@RestController
@RequestMapping("/secondHandGood")
public class SecondHandGoodController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecondHandGoodService secondHandGoodService;

    @ApiOperation("get one good's full information")
    @GetMapping("{goodId}")
    AjaxResult getGoodById(@PathVariable("goodId") Integer goodId) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandGoodService.getGoodById(goodId));
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to get the good's information.");
        }
    }

    @ApiOperation("list all goods of the current user")
    @GetMapping("current/all")
    AjaxResult listAllGoodsOfCurrent(Integer pageSize, Integer pageNo) {
        try {
            List<BriefGoodVO> briefGoodVOList = secondHandGoodService.getAllGoodsByPublisher(MiscUtils.currentUserId(), pageSize, pageNo);
            int total = secondHandGoodService.countAllGoodsByPublisher(MiscUtils.currentUserId());
            return AjaxResult.setSuccess(true).setData(new ListPage<>(briefGoodVOList, pageSize, pageNo, total));
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to list user's good.");
        }
    }

    @ApiOperation("list unsold goods of the current user")
    @GetMapping("current/unsold")
    AjaxResult listUnsoldGoodsOfCurrent(Integer pageSize, Integer pageNo) {
        try {
            List<BriefGoodVO> briefGoodVOList = secondHandGoodService.getUnsoldGoodsByPublisher(MiscUtils.currentUserId(), pageSize, pageNo);
            int total = secondHandGoodService.countUnsoldGoodsByPublisher(MiscUtils.currentUserId());
            return AjaxResult.setSuccess(true).setData(new ListPage<>(briefGoodVOList, pageSize, pageNo, total));
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to list user's good.");
        }
    }

    @ApiOperation("list all goods of other user")
    @GetMapping("other/all")
    AjaxResult listAllGoodsOfOther(Integer userId, Integer pageSize, Integer pageNo) {
        try {
            List<BriefGoodVO> briefGoodVOList = secondHandGoodService.getAllGoodsByPublisher(userId, pageSize, pageNo);
            int total = secondHandGoodService.countAllGoodsByPublisher(MiscUtils.currentUserId());
            return AjaxResult.setSuccess(true).setData(new ListPage<>(briefGoodVOList, pageSize, pageNo, total));
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to list user's good.");
        }
    }

    @ApiOperation("list unsold goods of other user")
    @GetMapping("other/unsold")
    AjaxResult listUnsoldGoodsOfOther(Integer userId, Integer pageSize, Integer pageNo) {
        try {
            List<BriefGoodVO> briefGoodVOList = secondHandGoodService.getUnsoldGoodsByPublisher(userId, pageSize, pageNo);
            int total = secondHandGoodService.countUnsoldGoodsByPublisher(MiscUtils.currentUserId());
            return AjaxResult.setSuccess(true).setData(new ListPage<>(briefGoodVOList, pageSize, pageNo, total));
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to list user's good.");
        }
    }

    @ApiOperation("get all the good types")
    @GetMapping("type/all")
    AjaxResult getAllGoodTypes() {
        try {
            List<GoodTypeVO> goodTypeVOList = secondHandGoodService.getAllGoodTypes();
            return AjaxResult.setSuccess(true).setData(goodTypeVOList);
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to get good types");
        }
    }

    @ApiOperation("get a good type by id")
    @GetMapping("type/{typeId}")
    AjaxResult getGoodTypeById(@PathVariable("typeId") Integer typeId) {
        try {
            GoodTypeVO goodTypeVO = secondHandGoodService.getGoodTypeById(typeId);
            return AjaxResult.setSuccess(true).setData(goodTypeVO);
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to get good type");
        }
    }

    @ApiOperation("create a new good")
    @PostMapping("new")
    AjaxResult createNewGood(@RequestBody SecondHandGoodDTO secondHandGood) {
        try {
            secondHandGood.setPublisher(MiscUtils.currentUserId());
            if (secondHandGoodService.insertSecondHandGood(secondHandGood) > 0) {
                return AjaxResult.setSuccess(true).setMsg("Good created successfully.");
            } else {
                return AjaxResult.setSuccess(false).setMsg("Failed to create new good.");
            }
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to create new good.");
        }
    }

    @ApiOperation("upload good pictures")
    @PostMapping("picture")
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

    @ApiOperation("update good information")
    @PutMapping("info")
    AjaxResult updateGoodInfo(@RequestBody SecondHandGoodDTO secondHandGood) {
        try {
            secondHandGood.setPublisher(MiscUtils.currentUserId());
            if (secondHandGoodService.updateGoodInfo(secondHandGood) > 0) {
                return AjaxResult.setSuccess(true).setMsg("Good information updated successfully.");
            } else {
                return AjaxResult.setSuccess(false).setMsg("Good information failed to update.");
            }
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Good information failed to update.");
        }
    }
}
