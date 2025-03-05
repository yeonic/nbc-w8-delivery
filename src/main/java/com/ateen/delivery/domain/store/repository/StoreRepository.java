package com.ateen.delivery.domain.store.repository;

import com.ateen.delivery.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * 특정 사장님이 소유한 가게 개수를 조회 (최대 3개 제한)
     * 가게 목록 조회
     * 상세 가게 조회
     */

    long countByOwnerId(Long ownerId);

    Page<Store> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Store> findByIdAndIsDeletedFalse(Long storeId);
}
