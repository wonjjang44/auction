package com.tasksprints.auction.domain.wallet.service;

import com.tasksprints.auction.domain.wallet.model.Wallet;
import com.tasksprints.auction.domain.wallet.repository.WalletRepository;
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


}
