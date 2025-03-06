package com.ateen.delivery.domain.store.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.entity.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private Address address;
    private String notice;
    private int estimatedPickupTime;
    private int minOrderAmount;
    private int deliveryTip;
    private boolean isOpen;
    private boolean isDeleted;
    private List<StoreBusinessHourResponse> businessHours;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<StoreHolidayResponse> holidays;

    public static StoreResponse from(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .address(store.getAddress())
                .notice(store.getNotice())
                .estimatedPickupTime(store.getEstimatedPickupTime())
                .minOrderAmount(store.getMinOrderAmount())
                .deliveryTip(store.getDeliveryTip())
                .isOpen(store.isOpen())
                .isDeleted(store.isDeleted())
                .businessHours(store.getBusinessHours() != null ?
                        store.getBusinessHours().stream()
                                .map(StoreBusinessHourResponse::from)
                                .collect(Collectors.toList())
                        : List.of())

                .build();
    }
}
