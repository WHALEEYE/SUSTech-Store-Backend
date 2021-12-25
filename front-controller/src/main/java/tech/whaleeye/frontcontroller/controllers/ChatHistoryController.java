package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.service.ChatHistoryService;

import java.util.Date;

@Api("chat history controller")
@RestController
@RequestMapping("/chatHistory")
@Log4j2
public class ChatHistoryController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @ApiOperation("get chat history")
    @GetMapping("/{userId}")
    AjaxResult getChatHistory(@PathVariable("userId") Integer userId,
                              @RequestParam Long beginTime,
                              @RequestParam Long endTime) {
        try {
            log.info("查询历史参数 {} {} {}", userId, beginTime, endTime);
            AjaxResult result = AjaxResult.setSuccess(true).setData(chatHistoryService.listChatHistory(MiscUtils.currentUserId(), userId, new Date(beginTime), new Date(endTime)));
            log.info(result);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list the chat history");
        }
    }


}
