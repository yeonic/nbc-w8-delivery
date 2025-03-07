package com.ateen.delivery.domain.store.dto.response;

import com.ateen.delivery.domain.common.vo.Address;
import com.ateen.delivery.domain.review.dto.response.ReviewResponse;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.domain.store.entity.category.StoreCategoryType;
import com.ateen.delivery.domain.store.entity.holiday.StoreHoliday;
import com.ateen.delivery.domain.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private List<StoreCategoryType> categories;
    private Address address;
    private String notice;
    private String businessNumber;
    private int estimatedPickupTime;
    private int minOrderAmount;
    private int deliveryTip;
    private boolean isOpen;
    private boolean isDeleted;
    private List<StoreBusinessHourResponse> businessHours;
    private List<ReviewResponse> reviews;
    private List<StoreHolidayResponse> holidays;

    public static StoreResponse from(Store store) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .businessNumber(store.getBusinessNumber())
                .categories(store.getCategories())
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
                                .collect(Collectors.toList()) : List.of())
                .build();
    }

    public static StoreResponse from(Store store, List<Review> reviews, List<StoreHoliday> holidays) {
        return StoreResponse.builder()
                .id(store.getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .businessNumber(store.getBusinessNumber())
                .categories(store.getCategories())
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
                                .collect(Collectors.toList()) : List.of())
                .reviews(reviews != null && !reviews.isEmpty() ?
                        reviews.stream().map(ReviewResponse::from).collect(Collectors.toList()) : null)
                .holidays(holidays != null && !holidays.isEmpty() ?
                        holidays.stream().map(StoreHolidayResponse::from).collect(Collectors.toList()) : null)
                .build();
    }
}

