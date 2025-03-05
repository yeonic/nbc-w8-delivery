package com.ateen.delivery.domain.store.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.dto.request.StoreUpdateRequest;
import com.ateen.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "store")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // 사장님 (유저)

    @Column(length = 30, nullable = false)
    private String name; // 가게 이름

    @Embedded
    private Address address; // ✅ Address 객체 사용

    @Column(length = 20, nullable = false)
    private String phoneNumber; // 가게 전화번호

    @Column(nullable = false)
    private LocalTime openTime; // 오픈 시간

    @Column(nullable = false)
    private LocalTime closeTime; // 마감 시간

    @Column(nullable = false)
    private int estimatedPickupTime; // 예상 수령 시간 (분 단위)

    @Column(nullable = false)
    private int minOrderAmount; // 최소 주문 금액

    @Column(nullable = false)
    private int deliveryTip; // 배달 팁

    @Column(nullable = false)
    private boolean isOpen; // 영업 여부

    @Column(nullable = false)
    private boolean isDeleted; // 삭제 여부 (soft delete)

    @Column(length = 1000)
    private String notice; // 공지글

    public void update(StoreUpdateRequest request) {
        this.name = request.getName();
        this.address = new Address(
                request.getCity(),
                request.getDistrict(),
                request.getStreet(),
                request.getDetail()
        );
        this.phoneNumber = request.getPhoneNumber();
        this.openTime = request.getOpenTime();
        this.closeTime = request.getCloseTime();
        this.estimatedPickupTime = request.getEstimatedPickupTime();
        this.minOrderAmount = request.getMinOrderAmount();
        this.deliveryTip = request.getDeliveryTip();
        this.notice = request.getNotice();
    }

    public void updateMinOrderAmount(int minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }
}
