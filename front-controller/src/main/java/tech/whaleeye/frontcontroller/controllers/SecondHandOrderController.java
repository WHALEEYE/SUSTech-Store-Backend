package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.dto.SecondHandOrderDTO;
import tech.whaleeye.model.vo.OrderVO;
import tech.whaleeye.service.SecondHandOrderService;

import java.math.BigDecimal;

@Api("Second Hand Order Controller")
@RestController
@RequestMapping("/secondHandOrder")
public class SecondHandOrderController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SecondHandOrderService secondHandOrderService;

    @ApiOperation("get order by order id")
    @GetMapping("detail/{orderId}")
    AjaxResult getOrderById(@PathVariable("orderId") Integer orderId) {
        try {
            Integer userType = secondHandOrderService.checkIdentity(MiscUtils.currentUserId(), orderId);
            OrderVO order = modelMapper.map(secondHandOrderService.getOrderById(orderId), OrderVO.class);
            order.setUserType(userType);
            return AjaxResult.setSuccess(true).setData(order);
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("Not allowed to check the order");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to get the info of the order");
        }
    }

    @ApiOperation("list buying orders of the current user")
    @GetMapping("buying")
    AjaxResult listBuyingOrdersOfCurrent(Integer status, Integer pageSize, Integer pageNo) {

        return null;
    }

    @ApiOperation("list selling orders of the current user")
    @GetMapping("selling")
    AjaxResult listSellingOrdersOfCurrent(Integer status, Integer pageSize, Integer pageNo) {

        return null;
    }

    @ApiOperation("create a new order")
    @PostMapping("new")
    AjaxResult createOrder(@RequestBody SecondHandOrderDTO secondHandOrder) {
        return null;
    }

    @ApiOperation("seller's acknowledge")
    @PutMapping("status/seller/{orderId}")
    AjaxResult sellerAcknowledge(@PathVariable("orderId") Integer orderId, Boolean ack, BigDecimal actualPrice) {
        return null;
    }

    @ApiOperation("buyer's acknowledge")
    @PutMapping("status/buyer/{orderId}")
    AjaxResult buyerAcknowledge(@PathVariable("orderId") Integer orderId, Boolean ack) {
        return null;
    }

    @ApiOperation("deal succeeded")
    @PutMapping("status/deal/{orderId}")
    AjaxResult confirmDeal(@PathVariable("orderId") Integer orderId, String dealCode) {
        return null;
    }

    @ApiOperation("deal refunded")
    @PutMapping("status/refund/{orderId}")
    AjaxResult refundDeal(@PathVariable("orderId") Integer orderId, String refundCode) {
        return null;
    }

    @ApiOperation("seller's comment and grade")
    @PutMapping("comment/seller/{orderId}")
    AjaxResult sellerComment(@PathVariable("orderId") Integer orderId, Integer grade, String comment) {
        return null;
    }

    @ApiOperation("buyer's comment and grade")
    @PutMapping("comment/buyer/{orderId}")
    AjaxResult buyerComment(@PathVariable("orderId") Integer orderId, Integer grade, String comment) {
        return null;
    }
}
