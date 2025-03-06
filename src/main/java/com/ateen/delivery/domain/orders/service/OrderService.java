package com.ateen.delivery.domain.orders.service;

import com.ateen.delivery.domain.common.exception.NotFoundException;
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
import com.ateen.delivery.global.dto.paging.PagingCondition;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    // TODO : TEMP_DELIVERY_FEE는 Store에서 가져오는 deliveryFee로 대체
    public static final int TEMP_DELIVERY_FEE = 1000;
    private final OrderRepository repository;

    public OrderResponse save(OrderCreateRequest request, LocalDateTime createdTime) {
        // TODO : user, store, menu를 찾아온다
        // 영업 시간, 최소 주문 금액 예외처리

        int deliveryFee = request.getOrderType() == OrderType.DELIVERY ? TEMP_DELIVERY_FEE : 0;
        Address targetAddress =
                new Address(request.getCity(), request.getDistrict(), request.getStreet(), request.getDetail());

        Order newOrder = Order.builder()
                .orderType(request.getOrderType())
                .address(targetAddress)
                .deliveryFee(deliveryFee)
                .amount(request.getAmount())
                .createdAt(createdTime)
                .build();
        return OrderResponse.fromOrder(repository.save(newOrder));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> findAll(PagingCondition pagingCondition) {
        Pageable pageRequest = PagingCondition.toPageRequest(pagingCondition);
        /** TODO
         * user가 생성했거나, user의 가게에 들어온 주문을 찾는다.
         * pagination을 적용하여 반환한다.
         */
        return repository.findAll(pageRequest).map(OrderResponse::fromOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrder(String orderNum) {
        // TODO : user가 생성했거나, user의 가게에 들어온 주문을 찾도록 조건 변경

        Order findOrder = repository.findById(orderNum)
                .orElseThrow(() -> new NotFoundException("사용자가 접근할 수 있는 주문이 없습니다."));
        return OrderResponse.fromOrder(findOrder);
    }

    public OrderResponse updateOrder(String orderNum, OrderModiRequest request) {
        // TODO : 현재 로그인된 유저가 주문을 생성한 유저인지 확인

        Order findOrder = repository.findById(orderNum).orElseThrow(
                () -> new NoSuchElementException("대상이 존재하지 않습니다.")
        );

        if (findOrder.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("주문은 수락 대기 상태에서만 변경이 가능합니다.");
        }

        // TODO : OrderType이 변경됨에 따라, 주소도 변경되도록.
        updateOrder(request, findOrder);
        return OrderResponse.fromOrder(findOrder);
    }

    public OrderResponse cancelOrder(String orderNum) {
        // TODO : USER/OWNER에 따라 CANCEL/REJECT가 결정되도록

        Order findOrder = repository.findById(orderNum).orElseThrow(
                () -> new NotFoundException("대상이 존재하지 않습니다.")
        );

        if (findOrder.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("주문은 수락 대기 상태에서만 변경이 가능합니다.");
        }

        findOrder.setOrderStatus(OrderStatus.CANCEL);
        return OrderResponse.fromOrder(findOrder);
    }

    @Transactional(readOnly = true)
    public OrderStatusResponse getOrderStatus(String orderNum) {
        // TODO : 주문 당사자들 이외의 사용자가 접근을 시도하는지

        Order findOrder = repository.findById(orderNum).orElseThrow(
                () -> new NotFoundException("사용자가 접근할 수 있는 주문이 없습니다.")
        );
        return OrderStatusResponse.fromOrders(findOrder);
    }

    public OrderStatusResponse updateOrderStatus(
            String orderNum, OrderStatusModiRequest request, LocalDateTime modifiedTime
    ) {
        // TODO : OWNER가 아닌 사용자가 접근을 시도하는지

        Order findOrder = repository.findById(orderNum).orElseThrow(
                () -> new NotFoundException("사용자가 접근할 수 있는 주문이 없습니다.")
        );

        if (request.getOrderStatus() == OrderStatus.DEPART) {
            findOrder.setPickupAt(modifiedTime);
            findOrder.setDeliveryStatus(DeliveryStatus.ONROAD);
        }

        if (request.getDeliveryStatus() == DeliveryStatus.DONE) {
            findOrder.setDeliveryDoneAt(modifiedTime);
            findOrder.setOrderStatus(OrderStatus.DEPART);
        }

        updateOrderStatus(request, findOrder);
        return OrderStatusResponse.fromOrders(findOrder);
    }

    public void delete(String orderNum) {
        // TODO : 주문을 생성한 유저가 삭제를 요청하는지
        Order findOrder = repository.findById(orderNum).orElseThrow(
                () -> new NoSuchElementException("대상이 존재하지 않습니다.")
        );

        if (findOrder.getDeliveryStatus() != DeliveryStatus.DONE || findOrder.getOrderStatus() != OrderStatus.CANCEL) {
            throw new IllegalStateException("완료된 주문 건만 삭제가 가능합니다.");
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
        findOrder.updateOrderStatus(orderStatus, deliveryStatus);
    }

}
