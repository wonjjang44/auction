package com.tasksprints.auction.domain.wallet.repository;

import com.tasksprints.auction.domain.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT u.wallet FROM users u WHERE u.id = :userId")
    Wallet getWalletByUserId(@Param("userId") Long userId);
}
