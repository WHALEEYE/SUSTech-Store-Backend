package tech.whaleeye.frontcontroller.controllers;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.whaleeye.misc.ajax.AjaxResult;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.misc.exceptions.BadOrderStatusException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.model.dto.SecondHandOrderDTO;
import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.model.vo.SecondHandOrder.OrderVO;
import tech.whaleeye.service.SecondHandOrderService;

import java.math.BigDecimal;

@Api("Second Hand Order Controller")
@RestController
@RequestMapping("/order")
public class SecondHandOrderController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SecondHandOrderService secondHandOrderService;

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

    @ApiOperation("list orders of the current user")
    @GetMapping("brief")
    AjaxResult listOrders(@RequestParam @ApiParam("false: seller; true: buyer") Boolean userType,
                          @RequestParam Integer orderStatus,
                          @RequestParam Integer pageSize,
                          @RequestParam Integer pageNo) {
        try {
            PageList<OrderVO> orderList = secondHandOrderService.getOrderByUserId(MiscUtils.currentUserId(), userType, orderStatus, pageSize, pageNo);
            return AjaxResult.setSuccess(true).setData(orderList);
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Failed to get the info of the orders");
        }
    }

    @ApiOperation("list orders of one good")
    @GetMapping("good/{goodId}")
    AjaxResult listOrdersOfGood(@PathVariable("goodId") Integer goodId,
                                @RequestParam Integer pageSize,
                                @RequestParam Integer pageNo) {
        try {
            PageList<OrderVO> orderList = secondHandOrderService.getOrderByGoodId(MiscUtils.currentUserId(), goodId, pageSize, pageNo);
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
    @PatchMapping("status/seller/{orderId}")
    AjaxResult sellerAcknowledge(@PathVariable("orderId") Integer orderId,
                                 @RequestParam Boolean ack,
                                 @RequestParam(required = false) BigDecimal actualPrice) {
        try {
            if (ack) {
                if (secondHandOrderService.sellerAcknowledge(MiscUtils.currentUserId(), orderId, actualPrice)) {
                    return AjaxResult.setSuccess(true).setMsg("Order acknowledged");
                } else {
                    return AjaxResult.setSuccess(false).setMsg("Operation failed");
                }
            } else {
                if (secondHandOrderService.sellerCancel(MiscUtils.currentUserId(), orderId)) {
                    return AjaxResult.setSuccess(true).setMsg("Canceled successfully");
                } else {
                    return AjaxResult.setSuccess(false).setMsg("Operation failed");
                }
            }
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("You are not allowed to this operation");
        } catch (BadOrderStatusException bose) {
            return AjaxResult.setSuccess(false).setMsg("Bad order status");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Operation failed");
        }
    }

    @ApiOperation("buyer's acknowledge")
    @PatchMapping("status/buyer/{orderId}")
    AjaxResult buyerAcknowledge(@PathVariable("orderId") Integer orderId,
                                @RequestParam Boolean ack) {
        try {
            if (ack) {
                if (secondHandOrderService.buyerAcknowledge(MiscUtils.currentUserId(), orderId)) {
                    return AjaxResult.setSuccess(true).setMsg("Paid successfully. The trade has established.");
                } else {
                    return AjaxResult.setSuccess(false).setMsg("Insufficient balance!");
                }
            } else {
                if (secondHandOrderService.buyerCancel(MiscUtils.currentUserId(), orderId)) {
                    return AjaxResult.setSuccess(false).setMsg("Canceled successfully");
                } else {
                    return AjaxResult.setSuccess(false).setMsg("Operation failed");
                }
            }
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
    @PatchMapping("status/deal/{orderId}")
    AjaxResult confirmDeal(@PathVariable("orderId") Integer orderId,
                           @RequestParam String dealCode) {
        try {
            if (!secondHandOrderService.confirmDeal(MiscUtils.currentUserId(), orderId, dealCode)) {
                return AjaxResult.setSuccess(false).setMsg("Deal confirmation failed");
            }
            return AjaxResult.setSuccess(false).setMsg("Deal confirmed");
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("You are not allowed to this operation");
        } catch (BadOrderStatusException bose) {
            return AjaxResult.setSuccess(false).setMsg("Bad order status");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Deal confirmation failed");
        }
    }

    @ApiOperation("deal refunded")
    @PatchMapping("status/refund/{orderId}")
    AjaxResult refundDeal(@PathVariable("orderId") Integer orderId,
                          @RequestParam String refundCode) {
        try {
            if (!secondHandOrderService.refundDeal(MiscUtils.currentUserId(), orderId, refundCode)) {
                return AjaxResult.setSuccess(false).setMsg("Refund failed");
            }
            return AjaxResult.setSuccess(false).setMsg("Refund succeeded");
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("You are not allowed to this operation");
        } catch (BadOrderStatusException bose) {
            return AjaxResult.setSuccess(false).setMsg("Bad order status");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Operation failed");
        }
    }

    @ApiOperation("seller's comment and grade")
    @PatchMapping("comment/{orderId}")
    AjaxResult leaveComment(@PathVariable("orderId") Integer orderId,
                            @RequestParam Integer grade,
                            @RequestParam String comment) {
        try {
            if (!secondHandOrderService.leaveComment(MiscUtils.currentUserId(), orderId, grade, comment)) {
                return AjaxResult.setSuccess(false).setMsg("Comment failed");
            }
            return AjaxResult.setSuccess(true).setMsg("Comment succeeded");
        } catch (BadIdentityException bie) {
            return AjaxResult.setSuccess(false).setMsg("You are not allowed to this operation");
        } catch (BadOrderStatusException bose) {
            return AjaxResult.setSuccess(false).setMsg("Bad order status");
        } catch (Exception e) {
            return AjaxResult.setSuccess(false).setMsg("Comment failed");
        }
    }

}
