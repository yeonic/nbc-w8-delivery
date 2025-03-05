package com.ateen.delivery.domain.orders.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.entity.Order;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class OrderStatusResponse {

    private String orderId;
    private Address targetAddress;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime createdAt;
    private LocalDateTime pickupAt;
    private LocalDateTime deliveryDoneAt;

    private OrderStatusResponse(String orderId, Address address, OrderStatus orderStatus,
            DeliveryStatus deliveryStatus, LocalDateTime createdAt, LocalDateTime pickupAt,
            LocalDateTime deliveryDoneAt) {

        // TODO : orderNum 진짜 값으로 대체

        this.orderId = orderId;
        this.targetAddress = Address.clone(address);
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.createdAt = createdAt;
        this.pickupAt = pickupAt;
        this.deliveryDoneAt = deliveryDoneAt;
    }

    public static OrderStatusResponse fromOrders(Order order) {
        return new OrderStatusResponse(
                order.getId(), order.getTargetAddress(), order.getOrderStatus(),
                order.getDeliveryStatus(), order.getCreatedAt(), order.getPickupAt(), order.getDeliveryDoneAt());
    }
}
