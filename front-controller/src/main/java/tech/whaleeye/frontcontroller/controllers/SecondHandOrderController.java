package tech.whaleeye.frontcontroller.controllers;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.misc.exceptions.BadOrderStatusException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.dto.SecondHandOrderDTO;
import tech.whaleeye.model.entity.SecondHandOrder;
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
            OrderVO order = modelMapper.map(secondHandOrderService.getOrderById(MiscUtils.currentUserId(), orderId), OrderVO.class);
            return AjaxResult.setSuccess(true).setData(order);
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("Not allowed to check the order");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to get the info of the order");
        }
    }

    @ApiOperation("list buying orders of the current user")
    @GetMapping("brief")
    AjaxResult listOrdersOfCurrent(@ApiParam("false: seller; true: buyer") Boolean userType,
                                   Integer orderStatus, Integer pageSize, Integer pageNo) {
        try {
            ListPage<OrderVO> orderList = secondHandOrderService.getOrderByUserId(MiscUtils.currentUserId(), userType, orderStatus, pageSize, pageNo);
            return AjaxResult.setSuccess(true).setData(orderList);
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to get the info of the orders");
        }
    }

    @ApiOperation("list orders of one good")
    @GetMapping("good/{goodId}")
    AjaxResult listOrdersOfGood(@PathVariable("goodId") Integer goodId, Integer pageSize, Integer pageNo) {
        try {
            ListPage<OrderVO> orderList = secondHandOrderService.getOrderByGoodId(MiscUtils.currentUserId(), goodId, pageSize, pageNo);
            return AjaxResult.setSuccess(true).setData(orderList);
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to get the info of the orders");
        }
    }

    @ApiOperation("create a new order")
    @PostMapping("new")
    AjaxResult createOrder(@RequestBody SecondHandOrderDTO secondHandOrderDTO) {
        try {
            SecondHandOrder secondHandOrder = modelMapper.map(secondHandOrderDTO, SecondHandOrder.class);
            secondHandOrder.setBuyerId(MiscUtils.currentUserId());
            if (secondHandOrderService.insertSecondHandOrder(secondHandOrder) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Failed to create the order");
            }
            return AjaxResult.setSuccess(true).setMsg("Order created successfully");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to create the order");
        }
    }

    @ApiOperation("seller's acknowledge")
    @PutMapping("status/seller/{orderId}")
    AjaxResult sellerAcknowledge(@PathVariable("orderId") Integer orderId, Boolean ack,
                                 @RequestParam(required = false) BigDecimal actualPrice) {
        try {
            if (secondHandOrderService.sellerAcknowledge(MiscUtils.currentUserId(), orderId, ack, actualPrice) <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Operation failed");
            }
            return AjaxResult.setSuccess(true).setMsg("Operation succeeded");
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("You are not allowed to this operation");
        } catch (BadOrderStatusException bose) {
            return AjaxResult.setSuccess(false).setMsg("Bad order status");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Operation failed");
        }
    }

    @ApiOperation("buyer's acknowledge")
    @PutMapping("status/buyer/{orderId}")
    AjaxResult buyerAcknowledge(@PathVariable("orderId") Integer orderId, Boolean ack) {
        try {
            int rst = secondHandOrderService.buyerAcknowledge(MiscUtils.currentUserId(), orderId, ack);
            if (ack && rst == -1) {
                return AjaxResult.setSuccess(false).setMsg("Insufficient balance!");
            } else if (!ack && rst <= 0) {
                return AjaxResult.setSuccess(false).setMsg("Operation failed.");
            }
            return AjaxResult.setSuccess(true).setMsg("Paid successfully. The trade has established.");
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("You are not allowed to this operation");
        } catch (BadOrderStatusException bose) {
            return AjaxResult.setSuccess(false).setMsg("Bad order status");
        } catch (TencentCloudSDKException tcse) {
            return AjaxResult.setSuccess(false).setMsg("Failed to send SMS. Please contact with the administrator.");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Operation failed");
        }
    }

    @ApiOperation("deal succeeded")
    @PutMapping("status/deal/{orderId}")
    AjaxResult confirmDeal(@PathVariable("orderId") Integer orderId, String dealCode) {
        try {
            return null;
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Operation failed");
        }
    }

    @ApiOperation("deal refunded")
    @PutMapping("status/refund/{orderId}")
    AjaxResult refundDeal(@PathVariable("orderId") Integer orderId, String refundCode) {
        try {
            return null;
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Operation failed");
        }
    }

    @ApiOperation("seller's comment and grade")
    @PutMapping("comment/seller/{orderId}")
    AjaxResult sellerComment(@PathVariable("orderId") Integer orderId, Integer grade, String comment) {
        try {
            return null;
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Operation failed");
        }
    }

    @ApiOperation("buyer's comment and grade")
    @PutMapping("comment/buyer/{orderId}")
    AjaxResult buyerComment(@PathVariable("orderId") Integer orderId, Integer grade, String comment) {
        try {
            return null;
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Operation failed");
        }
    }
}
