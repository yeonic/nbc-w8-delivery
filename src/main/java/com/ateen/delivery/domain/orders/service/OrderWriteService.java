package com.ateen.delivery.domain.orders.service;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.dto.request.OrderCreateRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderModiRequest;
import com.ateen.delivery.domain.orders.dto.request.OrderStatusModiRequest;
import com.ateen.delivery.domain.orders.dto.response.OrderResponse;
import com.ateen.delivery.domain.orders.dto.response.OrderStatusResponse;
import com.ateen.delivery.domain.orders.entity.Order;
import com.ateen.delivery.domain.orders.repository.OrderRepository;
import com.ateen.delivery.global.dto.error.ErrorCode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderWriteService {

    // TODO : TEMP_DELIVERY_FEE는 Store에서 가져오는 deliveryFee로 대체
    public static final int TEMP_DELIVERY_FEE = 1000;
    private final OrderRepository repository;

    public OrderResponse save(OrderCreateRequest request, LocalDateTime createdTime) {
        // TODO : user, store, menu를 찾아온다
        // 영업 시간, 최소 주문 금액 예외처리

        int deliveryFee = request.getOrderType() == OrderType.DELIVERY ? TEMP_DELIVERY_FEE : 0;
        Address targetAddress =
                new Address(request.getCity(), request.getDistrict(), request.getStreet(), request.getDetail());

        Order newOrder = Order.createOrder(
                request.getOrderType(), targetAddress, request.getAmount(), deliveryFee, createdTime
        );
        return OrderResponse.fromOrder(repository.save(newOrder));
    }

    public OrderResponse updateOrder(String orderNum, OrderModiRequest request) {
        // TODO : 현재 로그인된 유저가 주문을 생성한 유저인지 확인

        Order findOrder = repository.findById(orderNum)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        if (findOrder.getOrderStatus() != OrderStatus.PENDING) {
            throw new ClientException(ErrorCode.ORDER_NOT_ACCEPTABLE);
        }

        // TODO : OrderType이 변경됨에 따라, 주소도 변경되도록.
        updateOrder(request, findOrder);
        return OrderResponse.fromOrder(findOrder);
    }

    public OrderResponse cancelOrder(String orderNum) {
        // TODO : USER/OWNER에 따라 CANCEL/REJECT가 결정되도록

        Order findOrder = repository.findById(orderNum)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        if (findOrder.getOrderStatus() != OrderStatus.PENDING) {
            throw new ClientException(ErrorCode.ORDER_NOT_ACCEPTABLE);
        }

        findOrder.updateOrderStatus(OrderStatus.CANCEL);
        return OrderResponse.fromOrder(findOrder);
    }

    public OrderStatusResponse updateOrderStatus(
            String orderNum, OrderStatusModiRequest request, LocalDateTime modifiedTime
    ) {
        // TODO : OWNER가 아닌 사용자가 접근을 시도하는지

        Order findOrder = repository.findById(orderNum)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        if (request.getOrderStatus() == OrderStatus.DEPART) {
            findOrder.updateWhenDeparted(modifiedTime);
        }

        if (request.getDeliveryStatus() == DeliveryStatus.DONE) {
            findOrder.updateWhenDeliveryDone(modifiedTime);
        }

        updateOrderStatus(request, findOrder);
        return OrderStatusResponse.fromOrders(findOrder);
    }

    public void delete(String orderNum) {
        // TODO : 주문을 생성한 유저가 삭제를 요청하는지
        Order findOrder = repository.findById(orderNum)
                .orElseThrow(() -> new ClientException(ErrorCode.ORDER_NOT_FOUND));

        if (findOrder.getDeliveryStatus() != DeliveryStatus.DONE && findOrder.getOrderStatus() != OrderStatus.CANCEL) {
            throw new ClientException(ErrorCode.ORDER_IS_ALIVE);
        }
        repository.delete(findOrder);
    }

    private <T> T nullSafeValue(T requestValue, T originalValue) {
        return requestValue == null ? originalValue : requestValue;
    }

    private void updateOrder(OrderModiRequest request, Order findOrder) {
        OrderType newOrderType = nullSafeValue(request.getOrderType(), findOrder.getOrderType());
        Integer amount = nullSafeValue(request.getAmount(), findOrder.getAmount());
        findOrder.updateOrder(newOrderType, amount);
    }

    private void updateOrderStatus(OrderStatusModiRequest request, Order findOrder) {
        OrderStatus orderStatus = nullSafeValue(request.getOrderStatus(), findOrder.getOrderStatus());
        DeliveryStatus deliveryStatus = nullSafeValue(request.getDeliveryStatus(), findOrder.getDeliveryStatus());
        findOrder.updateOrderDeliveryStatus(orderStatus, deliveryStatus);
    }

}
