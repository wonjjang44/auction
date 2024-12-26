package com.tasksprints.auction.wallet.application.service;

import com.tasksprints.auction.wallet.domain.entity.Wallet;
import com.tasksprints.auction.wallet.infrastructure.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Override
    public void chargeMoney(Wallet wallet, BigDecimal amount) {
        wallet.chargeBalance(amount);
        walletRepository.save(wallet);
    }

    @Override
    public boolean isSufficientMoney() {
        return false;
    }

    @Override
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.getWalletByUserId(userId);
    }

}
