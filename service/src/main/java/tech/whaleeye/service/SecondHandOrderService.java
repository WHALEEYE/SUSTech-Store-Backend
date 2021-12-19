package tech.whaleeye.service;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.lang.Nullable;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.model.vo.SecondHandOrder.BackOrderVO;
import tech.whaleeye.model.vo.SecondHandOrder.OrderVO;

import java.math.BigDecimal;

public interface SecondHandOrderService {

    OrderVO getOrderById(Integer userId, Integer orderId);

    PageList<OrderVO> getOrderByUserId(Integer userId, Boolean userType, Integer orderStatus, Integer pageSize, Integer pageNo);

    PageList<OrderVO> getOrderByGoodId(Integer publisher, Integer goodId, Integer pageSize, Integer pageNo);

    Integer insertSecondHandOrder(SecondHandOrder secondHandOrder);

    boolean sellerAcknowledge(Integer userId, Integer orderId, BigDecimal actualPrice) throws TencentCloudSDKException;

    boolean sellerCancel(Integer userId, Integer orderId) throws TencentCloudSDKException;

    boolean buyerAcknowledge(Integer userId, Integer orderId) throws TencentCloudSDKException;

    boolean buyerCancel(Integer userId, Integer orderId) throws TencentCloudSDKException;

    boolean confirmDeal(Integer userId, Integer orderId, String dealCode) throws TencentCloudSDKException;

    boolean refundDeal(Integer userId, Integer orderId, String refundCode) throws TencentCloudSDKException;

    boolean leaveComment(Integer userId, Integer orderId, Integer grade, String comment);

    /* used in background system */

    PageList<BackOrderVO> listAllOrders(Integer pageSize, Integer pageNo, @Nullable Integer orderId);

}
