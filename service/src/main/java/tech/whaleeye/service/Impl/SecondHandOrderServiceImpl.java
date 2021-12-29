package tech.whaleeye.service.Impl;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.whaleeye.mapper.*;
import tech.whaleeye.misc.ajax.PageList;
import tech.whaleeye.misc.constants.CreditChangeEvents;
import tech.whaleeye.misc.constants.OrderState;
import tech.whaleeye.misc.exceptions.BadIdentityException;
import tech.whaleeye.misc.exceptions.BadOrderStatusException;
import tech.whaleeye.misc.exceptions.InvalidValueException;
import tech.whaleeye.misc.exceptions.LowCreditException;
import tech.whaleeye.misc.utils.MiscUtils;
import tech.whaleeye.misc.utils.TencentCloudUtils;
import tech.whaleeye.model.dto.SecondHandOrderDTO;
import tech.whaleeye.model.entity.SecondHandGood;
import tech.whaleeye.model.entity.SecondHandOrder;
import tech.whaleeye.model.entity.StoreUser;
import tech.whaleeye.model.vo.SecondHandOrder.BackOrderVO;
import tech.whaleeye.model.vo.SecondHandOrder.OrderVO;
import tech.whaleeye.service.SecondHandOrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SecondHandOrderServiceImpl implements SecondHandOrderService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SecondHandOrderMapper secondHandOrderMapper;

    @Autowired
    private SecondHandGoodMapper secondHandGoodMapper;

    @Autowired
    private StoreUserMapper storeUserMapper;

    @Autowired
    private InputHistoryMapper inputHistoryMapper;

    @Autowired
    private CreditHistoryMapper creditHistoryMapper;

    @Override
    public OrderVO getOrderById(Integer userId, Integer orderId) {
        Boolean userType = secondHandOrderMapper.getUserType(userId, orderId);
        if (userType == null) {
            throw new BadIdentityException();
        }
        OrderVO order = modelMapper.map(secondHandOrderMapper.getOrderById(orderId), OrderVO.class);
        order.setUserType(userType);
        return order;
    }

    @Override
    public PageList<OrderVO> getOrderByUserId(Integer userId, Boolean userType, Integer orderStatus, Integer pageSize, Integer pageNo) {
        List<OrderVO> orderList = modelMapper.map(secondHandOrderMapper.getOrderByUserId(userId, userType, orderStatus, pageSize, pageSize * (pageNo - 1)), new TypeToken<List<OrderVO>>() {
        }.getType());
        Integer total = secondHandOrderMapper.countOrderByUserId(userId, userType, orderStatus);
        return new PageList<>(orderList, pageSize, pageNo, total);
    }

    @Override
    public PageList<OrderVO> getOrderByGoodId(Integer goodId, Integer pageSize, Integer pageNo) {
        Integer publisher = MiscUtils.currentUserId();
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(goodId);
        if (!secondHandGood.getPublisher().equals(MiscUtils.currentUserId())) {
            throw new BadIdentityException();
        }
        List<OrderVO> orderList = modelMapper.map(secondHandOrderMapper.getOrderByGoodId(MiscUtils.currentUserId(), goodId, pageSize, pageSize * (pageNo - 1)), new TypeToken<List<OrderVO>>() {
        }.getType());
        Integer total = secondHandOrderMapper.countOrderByGoodId(publisher, goodId);
        return new PageList<>(orderList, pageSize, pageNo, total);
    }

    @Override
    public Integer insertSecondHandOrder(SecondHandOrderDTO secondHandOrderDTO) throws TencentCloudSDKException {
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrderDTO.getGoodId());
        if (storeUserMapper.getUserById(MiscUtils.currentUserId()).getCreditScore().doubleValue() < 85) {
            throw new LowCreditException();
        } else if (secondHandGood.getPublisher().equals(MiscUtils.currentUserId())) {
            throw new BadIdentityException();
        } else if (secondHandOrderMapper.alreadyHaveOrder(MiscUtils.currentUserId(), secondHandOrderDTO.getGoodId())) {
            throw new BadOrderStatusException();
        }
        SecondHandOrder secondHandOrder = modelMapper.map(secondHandOrderDTO, SecondHandOrder.class);
        secondHandOrder.setBuyerId(MiscUtils.currentUserId());
        secondHandOrder.setActualPrice(secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId()).getPrice());

        if (secondHandOrderMapper.insertSecondHandOrder(secondHandOrder) <= 0) {
            return null;
        }

        String phoneNumber = storeUserMapper.getUserById(secondHandGood.getPublisher()).getPhoneNumber();
        TencentCloudUtils.sendNewOrderInfo(phoneNumber, secondHandGood.getTitle());
        // TODO: Baidu Map Verify Location
        return secondHandOrder.getId();
    }

    @Override
    public boolean sellerAcknowledge(Integer userId, Integer orderId, BigDecimal actualPrice) throws TencentCloudSDKException {
        SecondHandOrder secondHandOrder = secondHandOrderMapper.getOrderById(orderId);
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
        if (!userId.equals(secondHandGood.getPublisher())) {
            throw new BadIdentityException();
        } else if (secondHandOrder.getOrderStatus() != OrderState.ACK_PENDING.ordinal()) {
            throw new BadOrderStatusException();
        } else if (actualPrice == null || actualPrice.doubleValue() < 0) {
            throw new InvalidValueException();
        }
        if (secondHandOrderMapper.updateActualPrice(orderId, actualPrice) <= 0) {
            return false;
        }

        String phoneNumber = storeUserMapper.getUserById(secondHandOrder.getBuyerId()).getPhoneNumber();
        TencentCloudUtils.sendRenewInfo(phoneNumber, secondHandGood.getTitle(), orderId, OrderState.PAY_PENDING);
        return true;
    }

    @Override
    public boolean sellerCancel(Integer userId, Integer orderId) throws TencentCloudSDKException {
        SecondHandOrder secondHandOrder = secondHandOrderMapper.getOrderById(orderId);
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
        if (!userId.equals(secondHandGood.getPublisher())) {
            throw new BadIdentityException();
        } else if (secondHandOrder.getOrderStatus() != OrderState.ACK_PENDING.ordinal()) {
            throw new BadOrderStatusException();
        }
        if (secondHandOrderMapper.cancelOrder(orderId) <= 0) {
            return false;
        }
        String phoneNumber = storeUserMapper.getUserById(secondHandOrder.getBuyerId()).getPhoneNumber();
        TencentCloudUtils.sendRenewInfo(phoneNumber, secondHandGood.getTitle(), orderId, OrderState.CANCELED);
        return true;
    }

    @Override
    public boolean buyerAcknowledge(Integer userId, Integer orderId) throws TencentCloudSDKException {
        SecondHandOrder secondHandOrder = secondHandOrderMapper.getOrderById(orderId);
        if (!userId.equals(secondHandOrder.getBuyerId())) {
            throw new BadIdentityException();
        } else if (secondHandOrder.getOrderStatus() != OrderState.PAY_PENDING.ordinal()) {
            throw new BadOrderStatusException();
        }

        String dealCode = String.format("%06d", new Random().nextInt(1000000));
        String refundCode = String.format("%06d", new Random().nextInt(1000000));
        String tradePassword = String.format("%04d", new Random().nextInt(10000));
        if (!secondHandOrderMapper.buyerAck(orderId, dealCode, refundCode, tradePassword)) {
            return false;
        }

        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
        String sellerNumber = storeUserMapper.getUserById(secondHandGood.getPublisher()).getPhoneNumber();
        String buyerNumber = storeUserMapper.getUserById(userId).getPhoneNumber();
        TencentCloudUtils.sendTradeEstablishedInfo(sellerNumber, buyerNumber, secondHandGood.getTitle(), orderId, tradePassword, dealCode, refundCode);
        return true;
    }

    @Override
    public boolean buyerCancel(Integer userId, Integer orderId) throws TencentCloudSDKException {
        SecondHandOrder secondHandOrder = secondHandOrderMapper.getOrderById(orderId);
        if (!userId.equals(secondHandOrder.getBuyerId())) {
            throw new BadIdentityException();
        } else if (secondHandOrder.getOrderStatus() != OrderState.PAY_PENDING.ordinal()) {
            throw new BadOrderStatusException();
        }
        if (secondHandOrderMapper.cancelOrder(orderId) <= 0) {
            return false;
        }
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
        String phoneNumber = storeUserMapper.getUserById(secondHandGood.getPublisher()).getPhoneNumber();
        TencentCloudUtils.sendRenewInfo(phoneNumber, secondHandGood.getTitle(), orderId, OrderState.CANCELED);
        return true;
    }

    @Override
    public boolean confirmDeal(Integer userId, Integer orderId, String dealCode) throws TencentCloudSDKException {
        SecondHandOrder secondHandOrder = secondHandOrderMapper.getOrderById(orderId);
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
        if (!userId.equals(secondHandGood.getPublisher())) {
            throw new BadIdentityException();
        } else if (secondHandOrder.getOrderStatus() != OrderState.TRADING.ordinal()) {
            throw new BadOrderStatusException();
        } else if (!dealCode.equals(secondHandOrder.getDealCode())) {
            if (inputHistoryMapper.countWrongInputTime(userId, orderId) >= 3) {
                creditHistoryMapper.changeCredit(userId, CreditChangeEvents.WRONG.getCode());
            }
            inputHistoryMapper.insertInputHistory(userId, orderId, dealCode, false);
            throw new InvalidValueException();
        }
        secondHandOrderMapper.orderConfirm(orderId);

        creditHistoryMapper.changeCredit(userId, CreditChangeEvents.TRADE.getCode());
        creditHistoryMapper.changeCredit(secondHandOrder.getBuyerId(), CreditChangeEvents.TRADE.getCode());

        inputHistoryMapper.insertInputHistory(userId, orderId, dealCode, true);

        String phoneNumber = storeUserMapper.getUserById(secondHandOrder.getBuyerId()).getPhoneNumber();
        TencentCloudUtils.sendRenewInfo(phoneNumber, secondHandGood.getTitle(), orderId, OrderState.DEAL);
        return true;
    }

    @Override
    public boolean refundDeal(Integer userId, Integer orderId, String refundCode) throws TencentCloudSDKException {
        SecondHandOrder secondHandOrder = secondHandOrderMapper.getOrderById(orderId);
        if (!userId.equals(secondHandOrder.getBuyerId())) {
            throw new BadIdentityException();
        } else if (secondHandOrder.getOrderStatus() != OrderState.TRADING.ordinal()) {
            throw new BadOrderStatusException();
        } else if (!refundCode.equals(secondHandOrder.getRefundCode())) {
            if (inputHistoryMapper.countWrongInputTime(userId, orderId) >= 3) {
                creditHistoryMapper.changeCredit(userId, CreditChangeEvents.WRONG.getCode());
            }
            inputHistoryMapper.insertInputHistory(userId, orderId, refundCode, false);
            throw new InvalidValueException();
        }

        secondHandOrderMapper.orderRefund(orderId);

        inputHistoryMapper.insertInputHistory(userId, orderId, refundCode, true);
        SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
        String phoneNumber = storeUserMapper.getUserById(secondHandOrder.getBuyerId()).getPhoneNumber();
        TencentCloudUtils.sendRenewInfo(phoneNumber, secondHandGood.getTitle(), orderId, OrderState.REFUND);
        return true;
    }

    @Override
    public boolean leaveComment(Integer userId, Integer orderId, Integer grade, String comment) {
        Boolean userType = secondHandOrderMapper.getUserType(userId, orderId);
        SecondHandOrder secondHandOrder = secondHandOrderMapper.getOrderById(orderId);
        if (userType == null) {
            throw new BadIdentityException();
        } else if (secondHandOrder.getOrderStatus() != OrderState.DEAL.ordinal()) {
            throw new BadOrderStatusException();
        } else if ((userType && secondHandOrder.getCommentByBuyer() != null) || (!userType && secondHandOrder.getCommentBySeller() != null)) {
            throw new BadOrderStatusException();
        } else if (comment.length() > 255 || grade < 1 || grade > 5) {
            throw new InvalidValueException();
        }

        if (grade >= 4) {
            if (userType) {
                SecondHandGood secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
                creditHistoryMapper.changeCredit(secondHandGood.getPublisher(), CreditChangeEvents.COMM.getCode());
            } else {
                creditHistoryMapper.changeCredit(secondHandOrder.getBuyerId(), CreditChangeEvents.COMM.getCode());
            }
        }
        return secondHandOrderMapper.updateCommentAndGrade(orderId, comment, grade, userType) > 0;
    }

    @Override
    public PageList<BackOrderVO> listAllOrders(Integer pageSize, Integer pageNo, Integer orderId) {
        List<SecondHandOrder> secondHandOrderList = secondHandOrderMapper.listAllOrders(pageSize, (pageNo - 1) * pageSize, orderId);
        StoreUser buyer, seller;
        BackOrderVO backOrderVO;
        SecondHandGood secondHandGood;
        List<BackOrderVO> backOrderVOList = new ArrayList<>();
        for (SecondHandOrder secondHandOrder : secondHandOrderList) {
            secondHandGood = secondHandGoodMapper.getGoodById(secondHandOrder.getGoodId());
            buyer = storeUserMapper.getUserById(secondHandOrder.getBuyerId());
            seller = storeUserMapper.getUserById(secondHandGood.getPublisher());
            backOrderVO = modelMapper.map(secondHandOrder, BackOrderVO.class);
            backOrderVO.setSellerNickname(seller.getNickname());
            backOrderVO.setSellerPhoneNumber(seller.getPhoneNumber());
            backOrderVO.setBuyerNickname(buyer.getNickname());
            backOrderVO.setBuyerPhoneNumber(buyer.getPhoneNumber());
            backOrderVO.setGoodTitle(secondHandGood.getTitle());
            backOrderVOList.add(backOrderVO);
        }
        int total = secondHandOrderMapper.countAllOrders(orderId);
        return new PageList<>(backOrderVOList, pageSize, pageNo, total);
    }

}
