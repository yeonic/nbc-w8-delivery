package com.ateen.delivery.domain.menu.repository;

import com.ateen.delivery.domain.menu.entity.Menu;
import com.ateen.delivery.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByStoreAndIsDeleted(Store store, Integer isDeleted);

    Optional<Menu> findByIdAndStoreAndIsDeleted(Long id, Store store, Integer isDeleted);
}
