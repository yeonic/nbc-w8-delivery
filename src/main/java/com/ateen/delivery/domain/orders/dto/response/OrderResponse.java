package com.ateen.delivery.domain.orders.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.orders.entity.Orders;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {

    private String orderNum;
    private String storeName;
    private String menuName;
    private Integer amount;
    private Integer deliveryFee;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Address targetAddress;

    private OrderResponse(Integer amount, Integer deliveryFee,
            OrderType orderType, OrderStatus orderStatus, Address address) {

        // TODO : orderNum, storeName, menuName 진짜 값으로 치환하기

        this.orderNum = "orderNum";
        this.storeName = "storeName";
        this.menuName = "menuName";
        this.amount = amount;
        this.deliveryFee = deliveryFee;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.targetAddress = new Address(
                address.getCity(), address.getDistrict(), address.getStreet(), address.getDetail());

    }

    public static OrderResponse fromOrders(Orders orders) {

        // TODO : orderNum, storeName, menuName 추가하기

        return new OrderResponse(
                orders.getAmount(), orders.getDeliveryFee(),
                orders.getOrderType(), orders.getOrderStatus(), orders.getTargetAddress()
        );
    }
}
