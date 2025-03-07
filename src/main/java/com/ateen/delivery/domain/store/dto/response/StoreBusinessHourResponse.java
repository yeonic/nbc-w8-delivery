package com.ateen.delivery.domain.store.dto.response;

import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreBusinessHourResponse {

    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isOpen;

    public static StoreBusinessHourResponse from(StoreBusinessHour businessHour) {
        return StoreBusinessHourResponse.builder()
                .dayOfWeek(businessHour.getDayOfWeek())
                .openTime(businessHour.isOpen() ? businessHour.getOpenTime() : null)
                .closeTime(businessHour.isOpen() ? businessHour.getCloseTime() : null)
                .isOpen(businessHour.isOpen())
                .build();
    }
}
