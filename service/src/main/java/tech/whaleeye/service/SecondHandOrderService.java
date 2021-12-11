package tech.whaleeye.service;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import tech.whaleeye.misc.ajax.ListPage;
import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.model.vo.OrderVO;

import java.math.BigDecimal;

public interface SecondHandOrderService {

    OrderVO getOrderById(Integer userId, Integer orderId);

    ListPage<OrderVO> getOrderByUserId(Integer userId, Boolean userType, Integer orderStatus, Integer pageSize, Integer pageNo);

    ListPage<OrderVO> getOrderByGoodId(Integer publisher, Integer goodId, Integer pageSize, Integer pageNo);

    Integer insertSecondHandOrder(SecondHandOrder secondHandOrder);

    boolean sellerAcknowledge(Integer userId, Integer orderId, BigDecimal actualPrice) throws TencentCloudSDKException;

    boolean sellerCancel(Integer userId, Integer orderId) throws TencentCloudSDKException;

    boolean buyerAcknowledge(Integer userId, Integer orderId) throws TencentCloudSDKException;

    boolean buyerCancel(Integer userId, Integer orderId) throws TencentCloudSDKException;

    boolean confirmDeal(Integer userId, Integer orderId, String dealCode) throws TencentCloudSDKException;

    boolean refundDeal(Integer userId, Integer orderId, String refundCode) throws TencentCloudSDKException;

    boolean leaveComment(Integer userId, Integer orderId, Integer grade, String comment);

}
