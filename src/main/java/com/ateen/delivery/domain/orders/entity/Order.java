package com.ateen.delivery.domain.orders.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.menu.entity.Menu;
import com.ateen.delivery.domain.orders.constants.DeliveryStatus;
import com.ateen.delivery.domain.orders.constants.OrderStatus;
import com.ateen.delivery.domain.orders.constants.OrderType;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private Address targetAddress;

    private int amount = 1;
    private int deliveryFee;

    private LocalDateTime pickupAt;
    private LocalDateTime deliveryDoneAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Order(OrderType orderType, OrderStatus orderStatus, DeliveryStatus deliveryStatus, Address address,
            Integer amount, int deliveryFee, LocalDateTime createdAt, Store store, User user, Menu menu) {
        super(createdAt, createdAt);
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.targetAddress = Address.clone(address);
        this.amount = amount;
        this.deliveryFee = deliveryFee;
        this.store = store;
        this.user = user;
        this.menu = menu;
    }

    public void updateOrder(OrderType orderType, Integer amount) {
        this.orderType = orderType;
        this.amount = amount;
    }


    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void updateDeliveryDoneAt(LocalDateTime deliveryDoneAt) {
        this.deliveryDoneAt = deliveryDoneAt;
    }

    public void updatePickupAt(LocalDateTime pickupAt) {
        this.pickupAt = pickupAt;
    }

    public void updateAddress(Address address) {
        this.targetAddress = Address.clone(address);
    }


    public static Order createOrder(
            OrderType orderType, Address address, Integer amount, int deliveryFee, LocalDateTime createdAt,
            Store store, User user, Menu menu
    ) {
        return new Order(orderType, OrderStatus.PENDING, DeliveryStatus.NOT_STARTED, address, amount, deliveryFee,
                createdAt, store, user, menu);
    }
}
