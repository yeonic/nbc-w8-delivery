package com.ateen.delivery.domain.store.dto.response;

import com.ateen.delivery.domain.store.entity.holiday.StoreHoliday;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreHolidayResponse {

    private boolean isSpecialHoliday;
    private LocalDate specialHolidayDate;
    private String holidayMessage;

    public static StoreHolidayResponse from(StoreHoliday storeHoliday) {
        return StoreHolidayResponse.builder()
                .isSpecialHoliday(storeHoliday.isSpecialHoliday())
                .specialHolidayDate(storeHoliday.getSpecialHolidayDate())
                .holidayMessage(storeHoliday.getHolidayMessage())
                .build();
    }
}
