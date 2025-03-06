package com.ateen.delivery.domain.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "store_business_hours")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoreBusinessHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "요일 정보는 필수 입력 값입니다.")
    private DayOfWeek dayOfWeek;

    @Column(nullable = true)
    private LocalTime openTime;

    @Column(nullable = true)
    private LocalTime closeTime;

    @NotNull(message = "영업 여부는 필수 입력 값입니다.")
    private boolean isOpen;

    public boolean isValid() {
        if (!isOpen) {
            return true;
        }
        return openTime != null && closeTime != null;
    }

    public void update(LocalTime openTime, LocalTime closeTime, boolean isOpen) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isOpen = isOpen;
    }
    @Builder
    public static StoreBusinessHour create(Store store, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, boolean isOpen) {
        return StoreBusinessHour.builder()
                .store(store)
                .dayOfWeek(dayOfWeek)
                .openTime(openTime)
                .closeTime(closeTime)
                .isOpen(isOpen)
                .build();
    }
}
