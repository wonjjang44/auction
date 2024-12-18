package com.tasksprints.auction.domain.wallet.service;

import com.tasksprints.auction.domain.wallet.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    void chargeMoney(Wallet wallet, BigDecimal amount);
    boolean isSufficientMoney();

    Wallet getWalletByUserId(Long userId);

}
