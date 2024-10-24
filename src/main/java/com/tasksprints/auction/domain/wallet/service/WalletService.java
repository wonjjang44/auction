package com.tasksprints.auction.domain.wallet.service;

public interface WalletService {
    //충전
    void chargeMoney();
    //잔액 충분한지?
    boolean isSufficientMoney();
}
