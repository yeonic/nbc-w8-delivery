package com.ateen.delivery.domain.orders.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.entity.Order;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {

    private String orderId;
    private String storeName;
    private String menuName;
    private Integer amount;
    private Integer deliveryFee;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Address targetAddress;
    private LocalDateTime createdAt;

    private OrderResponse(String orderId, Integer amount, Integer deliveryFee,
            OrderType orderType, OrderStatus orderStatus, Address address, LocalDateTime createdAt) {

        // TODO : orderNum, storeName, menuName 진짜 값으로 치환하기
        this.orderId = orderId;
        this.storeName = "storeName";
        this.menuName = "menuName";
        this.amount = amount;
        this.deliveryFee = deliveryFee;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.targetAddress = Address.clone(address);
        this.createdAt = createdAt;
    }

    public static OrderResponse fromOrders(Order order) {

        // TODO : orderNum, storeName, menuName 추가하기

        return new OrderResponse(
                order.getId(), order.getAmount(), order.getDeliveryFee(),
                order.getOrderType(), order.getOrderStatus(), order.getTargetAddress(),
                order.getCreatedAt()
        );
    }
}
