package com.tasksprints.auction.user.infrastructure;

import com.tasksprints.auction.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
