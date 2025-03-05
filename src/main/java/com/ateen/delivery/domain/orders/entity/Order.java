package com.ateen.delivery.domain.orders.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.constants.OrderType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Entity
@Table(name = "orders")
@SoftDelete(columnName = "is_deleted")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Setter private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Setter private DeliveryStatus deliveryStatus;

    private Address targetAddress;

    private int amount = 1;
    private int deliveryFee;

    @Setter private LocalDateTime pickupAt;
    @Setter private LocalDateTime deliveryDoneAt;
    
    // TODO: User, Store, Menu

    @Builder
    private Order(OrderType orderType, Address address, Integer amount, int deliveryFee) {
        this.orderType = orderType;
        this.orderStatus = OrderStatus.PENDING;
        this.deliveryStatus = DeliveryStatus.NOT_STARTED;
        this.targetAddress = new Address(
                address.getCity(), address.getDistrict(), address.getStreet(), address.getDetail());
        this.amount = amount;
        this.deliveryFee = deliveryFee;
    }

    public void updateOrder(OrderType orderType, Integer amount) {
        this.orderType = orderType;
        this.amount = amount;
    }

    public void updateOrderStatus(OrderStatus orderStatus, DeliveryStatus deliveryStatus) {
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
    }

    public void updateAddress(Address address) {
        this.targetAddress = Address.clone(address);
    }
}
