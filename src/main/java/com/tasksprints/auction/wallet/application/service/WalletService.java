package com.tasksprints.auction.wallet.application.service;

import com.tasksprints.auction.wallet.domain.entity.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    void chargeMoney(Wallet wallet, BigDecimal amount);
    boolean isSufficientMoney();

    Wallet getWalletByUserId(Long userId);

}
