package com.ateen.delivery.domain.store.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.entity.holiday.StoreHoliday;
import com.ateen.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.*;

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
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Embedded
    private Address address;

    private String notice;

    @Column(nullable = false)
    private int estimatedPickupTime;

    @Column(nullable = false)
    private int minOrderAmount;

    @Column(nullable = false)
    private int deliveryTip;

    @Column(nullable = false)
    private boolean isOpen;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreBusinessHour> businessHours = new ArrayList<>();

    public static Store createStore(User owner, String name, String phoneNumber, Address address, String notice,
                                    int estimatedPickupTime, int minOrderAmount, int deliveryTip, boolean isOpen, List<StoreBusinessHour> businessHours) {
        Store store = Store.builder()
                .owner(owner)
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .notice(notice)
                .estimatedPickupTime(estimatedPickupTime)
                .minOrderAmount(minOrderAmount)
                .deliveryTip(deliveryTip)
                .isOpen(isOpen)
                .isDeleted(false)
                .businessHours(new ArrayList<>())
                .build();

        List<StoreBusinessHour> updatedBusinessHours = businessHours.stream()
                .map(bh -> StoreBusinessHour.create(store, bh.getDayOfWeek(), bh.getOpenTime(), bh.getCloseTime(), bh.isOpen()))
                .toList();

        store.businessHours.addAll(updatedBusinessHours);

        return store;
    }

    public void update(String name, String phoneNumber, Address address, String notice) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.notice = notice;
    }

    public void updateMinOrderAmount(int minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void updateBusinessHours(List<StoreBusinessHour> newBusinessHours) {
        if (newBusinessHours == null || newBusinessHours.isEmpty()) {
            throw new IllegalArgumentException("영업 시간 정보는 비어 있을 수 없습니다.");
        }

        Set<DayOfWeek> uniqueDays = new HashSet<>();
        List<StoreBusinessHour> updatedBusinessHours = new ArrayList<>();

        for (StoreBusinessHour businessHour : newBusinessHours) {
            if (!uniqueDays.add(businessHour.getDayOfWeek())) {
                throw new IllegalArgumentException("중복된 요일의 영업 시간이 입력되었습니다: " + businessHour.getDayOfWeek());
            }

            updatedBusinessHours.add(StoreBusinessHour.builder()
                    .store(this)
                    .dayOfWeek(businessHour.getDayOfWeek())
                    .openTime(businessHour.getOpenTime())
                    .closeTime(businessHour.getCloseTime())
                    .isOpen(businessHour.isOpen())
                    .build());
        }

        this.businessHours.clear();
        this.businessHours.addAll(updatedBusinessHours);
    }
}
