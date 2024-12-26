package com.tasksprints.auction.auth.infrastructure;

import com.tasksprints.auction.auth.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
