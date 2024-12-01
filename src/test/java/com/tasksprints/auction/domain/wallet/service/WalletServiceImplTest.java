package com.tasksprints.auction.domain.wallet.service;

import com.tasksprints.auction.domain.wallet.model.Wallet;
import com.tasksprints.auction.domain.wallet.repository.WalletRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletRepository walletRepository;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = Wallet.builder()
            .id(1L)
            .balance(BigDecimal.ZERO)
            .userName("testUser")
            .build();
    }

    @Test
    void 충전_메서드_실행_시_지갑의_balance가_증가하면_성공한다() {
        //given
        BigDecimal amount = BigDecimal.valueOf(1000);
        //when
        walletService.chargeMoney(wallet, amount);
        //then
        assertEquals(amount, wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    @DisplayName("getWalletByUserId 테스트 ")
    void UserId로_Wallet이_조회되면_성공한다() {
        //given
        Long walletId = 1L;
        when(walletRepository.getWalletByUserId(walletId)).thenReturn(wallet);
        //when
        Wallet wallet = walletService.getWalletByUserId(1L);
        //then
        assertNotNull(wallet);
        assertEquals(walletId, wallet.getId());
    }
}
