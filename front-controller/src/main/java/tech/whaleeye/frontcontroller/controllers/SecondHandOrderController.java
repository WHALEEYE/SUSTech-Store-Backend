package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.model.dto.SecondHandOrderDTO;
import tech.whaleeye.service.SecondHandOrderService;

@Api("Second Hand Order Controller")
@RestController
@RequestMapping("/secondHandOrder")
public class SecondHandOrderController {

    @Autowired
    SecondHandOrderService secondHandOrderService;

    @ApiOperation("get order by order id")
    @GetMapping("detail/{orderId}")
    AjaxResult getOrderById(@PathVariable("orderId") Integer orderId) {
        return null;
    }

    @ApiOperation("list orders of the current user")
    @GetMapping("brief")
    AjaxResult listOrdersOfCurrent(Integer pageSize, Integer status, Integer pageNo) {
        return null;
    }

    @ApiOperation("list orders of other users")
    @GetMapping("brief/{userId}")
    AjaxResult listOrdersOfOthers(@PathVariable("userId") Integer userId, Integer status, Integer pageSize, Integer pageNo) {
        return null;
    }

    @ApiOperation("create a new order")
    @PostMapping("new")
    AjaxResult createOrder(@RequestBody SecondHandOrderDTO secondHandOrder) {
        return null;
    }

    //TODO: The APIs for the interaction between the seller and the buyer

}
