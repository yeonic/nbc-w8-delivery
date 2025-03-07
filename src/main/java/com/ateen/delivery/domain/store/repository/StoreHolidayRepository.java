package com.ateen.delivery.domain.store.repository;

import com.ateen.delivery.domain.store.entity.holiday.StoreHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreHolidayRepository extends JpaRepository<StoreHoliday, Long> {

    Optional<StoreHoliday> findByIdAndStoreId(Long id, Long storeId);

    List<StoreHoliday> findAllByStoreId(Long storeId);

    Optional<StoreHoliday> findByStoreIdAndSpecialHolidayDate(Long storeId, LocalDate specialHolidayDate);
}
