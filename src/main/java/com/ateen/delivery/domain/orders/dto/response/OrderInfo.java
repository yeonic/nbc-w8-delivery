package com.ateen.delivery.domain.orders.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class OrderInfo {

    private String orderId;
    private Address targetAddress;
    private OrderStatus orderStatus;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public OrderInfo(String orderId, Address targetAddress, OrderStatus orderStatus, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.targetAddress = Address.clone(targetAddress);
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }
}
