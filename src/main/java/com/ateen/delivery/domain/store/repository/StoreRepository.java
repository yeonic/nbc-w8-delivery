package com.ateen.delivery.domain.store.repository;

import com.ateen.delivery.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    long countByOwnerId(Long ownerId);

    @EntityGraph(attributePaths = {"owner"})
    Page<Store> findAllByIsDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"owner"})
    Optional<Store> findByIdAndIsDeletedFalse(Long storeId);

    Page<Store> findByNameContainingAndIsDeletedFalse(String name, Pageable pageable);

}
