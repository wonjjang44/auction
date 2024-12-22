package com.tasksprints.auction.domain.auth.repository;

import com.tasksprints.auction.domain.auth.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
