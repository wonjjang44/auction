package com.tasksprints.auction.domain.wallet.repository;

import com.tasksprints.auction.common.config.QueryDslConfig;
import com.tasksprints.auction.domain.user.model.User;
import com.tasksprints.auction.domain.user.repository.UserRepository;
import com.tasksprints.auction.domain.wallet.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import(QueryDslConfig.class)
class WalletRepositoryTest {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.createWithWallet(
            "testName",
            "testEmail",
            "testPassword",
            "testNickName"
        );
        user = userRepository.save(user);
    }

    @Test
    @DisplayName("getWalletByUserId 테스트 : JPQL로 UserId를 통해 조회했을 시 Wallet이 리턴되면 성공한다")
    void JPQL로_UserId를_통해_조회했을_시_Wallet이_리턴되면_성공한다() {
        //given
        Long userId = user.getId();
        //when
        Wallet foundWallet = walletRepository.getWalletByUserId(userId);
        //then
        assertNotNull(foundWallet);
        assertEquals(user.getWallet().getId(), foundWallet.getId());
    }

}
