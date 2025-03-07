package com.ateen.delivery.domain.store.repository;

import com.ateen.delivery.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @EntityGraph(attributePaths = {"owner"})
    Page<Store> findAllByIsDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"owner"})
    Optional<Store> findByIdAndIsDeletedFalse(Long storeId);

    Page<Store> findByNameContainingAndIsDeletedFalse(String name, Pageable pageable);

    boolean existsByBusinessNumber(String businessNumber);

    @Query("SELECT COUNT(s) FROM Store s WHERE s.owner.id = :ownerId AND s.isDeleted = false")
    long countByOwnerIdAndIsDeletedFalse(@Param("ownerId") Long ownerId);

    @Query("SELECT s.owner.id FROM Store s WHERE s.id = :storeId AND s.isDeleted = false")
    Optional<Long> findOwnerIdByStoreId(@Param("storeId") Long storeId);
}
