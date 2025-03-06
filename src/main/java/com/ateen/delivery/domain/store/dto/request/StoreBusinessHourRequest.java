package com.ateen.delivery.domain.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreBusinessHourRequest {

    @NotNull(message = "요일은 필수 입력 값입니다.")
    private DayOfWeek dayOfWeek;

    private LocalTime openTime;
    private LocalTime closeTime;

    @NotNull(message = "영업 여부는 필수 입력 값입니다.")
    private Boolean isOpen;

    public boolean isValid() {
        return !isOpen || (openTime != null && closeTime != null);
    }
}
