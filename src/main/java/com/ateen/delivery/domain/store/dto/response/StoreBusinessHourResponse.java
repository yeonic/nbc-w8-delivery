package com.ateen.delivery.domain.store.dto.response;

import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreBusinessHourResponse {

    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isOpen;

    public static StoreBusinessHourResponse from(StoreBusinessHour businessHour) {
        return StoreBusinessHourResponse.builder()
                .dayOfWeek(businessHour.getDayOfWeek())
                .openTime(businessHour.getOpenTime())
                .closeTime(businessHour.getCloseTime())
                .isOpen(businessHour.isOpen()) // ✅ `businessHour`에서 직접 가져옴
                .build();
    }
}
