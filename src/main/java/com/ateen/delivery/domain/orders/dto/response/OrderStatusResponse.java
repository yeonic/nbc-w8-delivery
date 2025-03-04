package com.ateen.delivery.domain.orders.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.entity.Orders;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class OrderStatusResponse {

    private String orderNum;
    private Address targetAddress;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime pickupAt;
    private LocalDateTime deliveryDoneAt;

    private OrderStatusResponse(Address address, OrderStatus orderStatus,
            DeliveryStatus deliveryStatus, LocalDateTime pickupAt, LocalDateTime deliveryDoneAt) {

        // TODO : orderNum 진짜 값으로 대체

        this.orderNum = "orderNum";
        this.targetAddress = new Address(
                address.getCity(), address.getDistrict(), address.getStreet(), address.getDetail());
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.pickupAt = pickupAt;
        this.deliveryDoneAt = deliveryDoneAt;
    }

    public static OrderStatusResponse fromOrders(Orders orders) {
        return new OrderStatusResponse(orders.getTargetAddress(), orders.getOrderStatus(), orders.getDeliveryStatus(),
                orders.getPickupAt(), orders.getDeliveryDoneAt());
    }
}
