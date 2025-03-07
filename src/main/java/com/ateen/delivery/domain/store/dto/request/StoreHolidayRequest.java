package com.ateen.delivery.domain.store.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreHolidayRequest {

    @NotNull(message = "비정기 휴무 여부를 입력해야 합니다.")
    private Boolean isSpecialHoliday;

    @Future(message = "휴무 날짜는 오늘 이후로만 설정 가능합니다.")
    private LocalDate specialHolidayDate;

    @Size(max = 300, message = "휴무일 메시지는 최대 300자까지 입력 가능합니다.")
    private String holidayMessage;
}
