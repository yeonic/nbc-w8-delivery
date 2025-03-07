package com.ateen.delivery.domain.store.entity;

import com.ateen.delivery.domain.common.entity.BaseEntity;
import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.entity.category.StoreCategoryType;
import com.ateen.delivery.domain.user.entity.User;
import com.ateen.delivery.global.dto.error.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
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

    @Column(nullable = false, length = 10, unique = true)
    @Pattern(regexp = "\\d{10}", message = "사업자번호는 10자리 숫자로 필수 입력해야 합니다.")
    private String businessNumber;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "store_categories", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "category", nullable = false)
    private List<StoreCategoryType> categories = new ArrayList<>();

    public static Store createStore(User owner, String name, String phoneNumber, Address address, String notice,
                                    String businessNumber, int estimatedPickupTime, int minOrderAmount, int deliveryTip, boolean isOpen,
                                    List<StoreBusinessHour> businessHours, List<StoreCategoryType> categories) {

        validateCategories(categories);

        return Store.builder()
                .owner(owner)
                .name(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .notice(notice)
                .businessNumber(businessNumber)
                .estimatedPickupTime(estimatedPickupTime)
                .minOrderAmount(minOrderAmount)
                .deliveryTip(deliveryTip)
                .isOpen(isOpen)
                .isDeleted(false)
                .businessHours(new ArrayList<>(businessHours))
                .categories(new ArrayList<>(categories))
                .build();
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
            throw new ClientException(ErrorCode.BUSINESS_HOUR_BLANK);
        }

        Set<DayOfWeek> uniqueDays = new HashSet<>();
        List<StoreBusinessHour> updatedBusinessHours = new ArrayList<>();

        for (StoreBusinessHour businessHour : newBusinessHours) {
            DayOfWeek day = businessHour.getDayOfWeek();

            if (!uniqueDays.add(day)) {
                throw new ClientException(ErrorCode.DUPLICATE_BUSINESS_HOUR, day);
            }

            if (businessHour.isOpen() && (businessHour.getOpenTime() == null || businessHour.getCloseTime() == null)) {
                throw new ClientException(ErrorCode.BUSINESS_HOUR_REQUIRED, day);
            }

            updatedBusinessHours.add(StoreBusinessHour.builder()
                    .store(this)
                    .dayOfWeek(day)
                    .openTime(businessHour.getOpenTime())
                    .closeTime(businessHour.getCloseTime())
                    .isOpen(businessHour.isOpen())
                    .build());
        }

        this.businessHours.clear();
        this.businessHours.addAll(updatedBusinessHours);
    }

    public void updateCategories(List<StoreCategoryType> newCategories) {
        validateCategories(newCategories);
        this.categories.clear();
        this.categories.addAll(newCategories);
    }

    private static void validateCategories(List<StoreCategoryType> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new ClientException(ErrorCode.CATEGORY_REQUIRED);
        }
        if (categories.size() > 3) {
            throw new ClientException(ErrorCode.CATEGORY_LIMIT_EXCEEDED);
        }
    }
}
