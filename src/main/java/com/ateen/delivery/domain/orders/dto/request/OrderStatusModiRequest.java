package com.ateen.delivery.domain.orders.dto.request;

import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class OrderStatusModiRequest {

    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime pickupAt;
    private LocalDateTime deliveryDoneAt;
}
