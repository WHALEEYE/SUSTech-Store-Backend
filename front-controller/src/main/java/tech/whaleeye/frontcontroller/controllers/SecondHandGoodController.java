package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.vo.BriefGoodVO;
import tech.whaleeye.service.SecondHandGoodService;

import java.util.ArrayList;
import java.util.List;

@Api("Second Hand Good Controller")
@RestController
@RequestMapping("/secondHandGood")
public class SecondHandGoodController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecondHandGoodService secondHandGoodService;

    @ApiOperation("list all goods of the current user")
    @GetMapping("current/all")
    AjaxResult listAllGoodsOfCurrent(Integer pageSize, Integer pageNo) {
        try {
            List<SecondHandGood> secondHandGoodList = secondHandGoodService.getAllGoodsByPublisher(MiscUtils.currentUserId(), pageSize, pageNo);
            int total = secondHandGoodService.countAllGoodsByPublisher(MiscUtils.currentUserId());
            List<BriefGoodVO> briefGoodVOList = new ArrayList<>(secondHandGoodList.size());
            BriefGoodVO briefGoodVO;
            for (SecondHandGood secondHandGood : secondHandGoodList) {
                briefGoodVO = modelMapper.map(secondHandGood, BriefGoodVO.class);
                briefGoodVOList.add(briefGoodVO);
            }
            return AjaxResult.setSuccess(true).setData(new ListPage<>(briefGoodVOList, pageSize, pageNo, total));
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to list user's good.");
        }
    }

    @ApiOperation("list all goods of other user")
    @GetMapping("other/all")
    AjaxResult listAllGoodsOfOther(Integer pageSize, Integer pageNo) {
        try {
            List<SecondHandGood> secondHandGoodList = secondHandGoodService.getAllGoodsByPublisher(MiscUtils.currentUserId(), pageSize, pageNo);
            int total = secondHandGoodService.countAllGoodsByPublisher(MiscUtils.currentUserId());
            return AjaxResult.setSuccess(true).setData(new ListPage<>(secondHandGoodList, pageSize, pageNo, total));
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to list user's good.");
        }
    }

}
