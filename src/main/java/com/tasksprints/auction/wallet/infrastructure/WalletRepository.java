package com.tasksprints.auction.wallet.infrastructure;

import com.tasksprints.auction.wallet.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT u.wallet FROM users u WHERE u.id = :userId")
    Wallet getWalletByUserId(@Param("userId") Long userId);
}
