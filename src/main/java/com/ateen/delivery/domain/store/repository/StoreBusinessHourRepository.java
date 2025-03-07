package com.ateen.delivery.domain.store.repository;

import com.ateen.delivery.domain.store.entity.StoreBusinessHour;
import com.ateen.delivery.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreBusinessHourRepository extends JpaRepository<StoreBusinessHour, Long> {
}
