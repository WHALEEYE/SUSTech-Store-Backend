package tech.whaleeye.backcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public AjaxResult listAllFrontUsers(Integer pageSize,
                                        Integer pageNo,
                                        @RequestParam(required = false) String searchNickname,
                                        @RequestParam(required = false) String searchPhoneNumber) {
        try {
            return AjaxResult.setSuccess(true).setData(storeUserService.listAll(pageSize, pageNo, searchNickname, searchPhoneNumber));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list front users");
        }
    }

}
