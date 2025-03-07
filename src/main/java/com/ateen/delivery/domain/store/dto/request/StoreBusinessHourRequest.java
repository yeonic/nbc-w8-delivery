package com.ateen.delivery.domain.store.dto.request;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.global.dto.error.ErrorCode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

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

    public void validate(Set<DayOfWeek> existingDays) {
        if (existingDays.contains(dayOfWeek)) {
            throw new ClientException(ErrorCode.DUPLICATE_BUSINESS_HOUR, dayOfWeek);
        }
        if (isOpen && (openTime == null || closeTime == null)) {
            throw new ClientException(ErrorCode.BUSINESS_HOUR_REQUIRED, dayOfWeek);
        }
        existingDays.add(dayOfWeek);
    }
}
