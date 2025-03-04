package com.ateen.delivery.domain.menu.repository;

import com.ateen.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findByIdAndStore(Long id, Store store);
}
