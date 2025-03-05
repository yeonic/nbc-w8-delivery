package com.ateen.delivery.domain.store.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.global.dto.Response;
import com.ateen.delivery.global.dto.paging.PagingResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private Address address;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int estimatedPickupTime;
    private int minOrderAmount;
    private int deliveryTip;
    private boolean isOpen;
    private String notice;

    public static StoreResponse from(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .address(store.getAddress())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .estimatedPickupTime(store.getEstimatedPickupTime())
                .minOrderAmount(store.getMinOrderAmount())
                .deliveryTip(store.getDeliveryTip())
                .isOpen(store.isOpen())
                .notice(store.getNotice())
                .build();
    }
}

//todo StoreListResponse와 StoreDetailResponse 구분 고려중
