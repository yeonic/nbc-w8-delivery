package com.ateen.delivery.domain.store.repository;

import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ateen.delivery.domain.store.entity.Store;

import java.util.List;

public interface StoreBusinessHourRepository extends JpaRepository<StoreBusinessHour, Long> {
    List<StoreBusinessHour> findByStore(Store store);

}

