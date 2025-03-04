package com.ateen.delivery.domain.orders.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.constants.OrderType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private OrderType orderType;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;

    private Address targetAddress;

    private int amount = 1;
    private int deliveryFee;

    private LocalDateTime pickupAt;
    private LocalDateTime deliveryDoneAt;

    private boolean isDeleted = false;

    // TODO: User, Store, Menu

    @Builder
    private Orders(OrderType orderType, OrderStatus orderStatus, DeliveryStatus deliveryStatus, Address address,
            Integer amount, int deliveryFee
    ) {
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.targetAddress = new Address(
                address.getCity(), address.getDistrict(), address.getStreet(), address.getDetail());
        this.amount = amount;
        this.deliveryFee = deliveryFee;
    }
}
