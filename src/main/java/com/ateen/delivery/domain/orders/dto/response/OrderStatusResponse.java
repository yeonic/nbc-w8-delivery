package com.ateen.delivery.domain.orders.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class OrderStatusResponse extends OrderInfo {

    private DeliveryStatus deliveryStatus;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime pickupAt;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deliveryDoneAt;

    private OrderStatusResponse(String orderId, Address address, OrderStatus orderStatus,
            DeliveryStatus deliveryStatus, LocalDateTime createdAt, LocalDateTime pickupAt,
            LocalDateTime deliveryDoneAt) {

        super(orderId, address, orderStatus, createdAt);
        this.deliveryStatus = deliveryStatus;
        this.pickupAt = pickupAt;
        this.deliveryDoneAt = deliveryDoneAt;
    }

    public static OrderStatusResponse fromOrders(Order order) {
        return new OrderStatusResponse(
                order.getId(), order.getTargetAddress(), order.getOrderStatus(),
                order.getDeliveryStatus(), order.getCreatedAt(), order.getPickupAt(), order.getDeliveryDoneAt());
    }
}
