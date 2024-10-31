package com.tasksprints.auction.domain.wallet.service;

import com.tasksprints.auction.domain.wallet.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {
    //충전
    void chargeMoney(Wallet wallet, BigDecimal amount);
    //잔액 충분한지?
    boolean isSufficientMoney();
}
