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
import tech.whaleeye.service.SecondHandOrderService;

@Api("second hand order controller")
@RestController
@RequestMapping("/order")
@Log4j2
public class SecondHandOrderController {

    @Autowired
    private SecondHandOrderService secondHandOrderService;

    @ApiOperation("list all the orders")
    @GetMapping("/all")
    AjaxResult listAllOrders(@RequestParam Integer pageSize,
                             @RequestParam Integer pageNo,
                             @RequestParam(required = false) Integer orderId) {
        try {
            return AjaxResult.setSuccess(true).setData(secondHandOrderService.listAllOrders(pageSize, pageNo, orderId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.setSuccess(false).setMsg("Failed to list orders");
        }
    }


}
