package com.tasksprints.auction.domain.wallet.repository;

import com.tasksprints.auction.domain.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
