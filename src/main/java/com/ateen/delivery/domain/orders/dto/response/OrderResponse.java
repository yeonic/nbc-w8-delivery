package com.ateen.delivery.domain.orders.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.entity.Order;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class OrderResponse extends OrderInfo {

    private String storeName;
    private String menuName;
    private Integer amount;
    private Integer deliveryFee;
    private OrderType orderType;

    private OrderResponse(String orderId, String storeName, String menuName, Integer amount, Integer deliveryFee,
            OrderType orderType, OrderStatus orderStatus, Address address, LocalDateTime createdAt) {

        super(orderId, address, orderStatus, createdAt);
        this.storeName = storeName;
        this.menuName = menuName;
        this.amount = amount;
        this.deliveryFee = deliveryFee;
        this.orderType = orderType;
    }

    public static OrderResponse fromOrder(Order order) {

        return new OrderResponse(
                order.getId(), order.getStore().getName(), order.getMenu().getName(), order.getAmount(),
                order.getDeliveryFee(),
                order.getOrderType(), order.getOrderStatus(), order.getTargetAddress(),
                order.getCreatedAt()
        );
    }
}
