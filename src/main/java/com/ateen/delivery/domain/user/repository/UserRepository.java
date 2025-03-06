package com.ateen.delivery.domain.user.repository;

import com.ateen.delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
