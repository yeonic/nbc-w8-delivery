package com.ateen.delivery.domain.store.entity;

import jakarta.persistence.*;
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
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column
    private LocalTime openTime;

    @Column
    private LocalTime closeTime;

    @Column(nullable = false)
    private boolean isOpen;

    public boolean isValid() {
        return !isOpen || (openTime != null && closeTime != null);
    }

    public void update(LocalTime openTime, LocalTime closeTime, boolean isOpen) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isOpen = isOpen;
    }
}
