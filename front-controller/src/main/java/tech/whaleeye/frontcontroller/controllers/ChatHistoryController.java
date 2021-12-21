package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @RequestMapping("/{userId}")
    public AjaxResult getChatHistory(@PathVariable("userId") Integer userId,
                                     @RequestParam Date beginTime,
                                     @RequestParam Date endTime) {
        try {
            return AjaxResult.setSuccess(true).setData(chatHistoryService.listChatHistory(MiscUtils.currentUserId(), userId, beginTime, endTime));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list the chat history");
        }
    }


}
