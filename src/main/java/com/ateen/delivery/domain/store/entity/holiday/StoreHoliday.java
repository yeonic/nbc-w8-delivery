package com.ateen.delivery.domain.store.entity.holiday;

import com.ateen.delivery.domain.common.exception.ClientException;
import com.ateen.delivery.domain.store.entity.Store;
import com.ateen.delivery.global.dto.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;

@Entity
@Table(name = "store_holidays")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoreHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private boolean isSpecialHoliday;

    @Column(nullable = false)
    private LocalDate specialHolidayDate;

    @Column(length = 300)
    private String holidayMessage;

    public static StoreHoliday createSpecialHoliday(Store store, LocalDate holidayDate, String holidayMessage) {
        if (holidayDate.isBefore(LocalDate.now())) {
            throw new ClientException(ErrorCode.HOLIDAY_DATE_INVALID);
        }
        return StoreHoliday.builder()
                .store(store)
                .isSpecialHoliday(true)
                .specialHolidayDate(holidayDate)
                .holidayMessage(holidayMessage)
                .build();
    }

    public void updateHoliday(LocalDate newDate, String newMessage) {
        if (newDate.isBefore(LocalDate.now())) {
            throw new ClientException(ErrorCode.HOLIDAY_DATE_INVALID);
        }
        this.specialHolidayDate = newDate;
        this.holidayMessage = newMessage;
    }
}

