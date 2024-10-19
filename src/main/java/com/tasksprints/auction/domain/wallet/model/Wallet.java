package com.tasksprints.auction.domain.wallet.model;

import com.tasksprints.auction.common.entity.BaseEntityWithUpdate;
import com.tasksprints.auction.domain.payment.model.Payment;
import com.tasksprints.auction.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends BaseEntityWithUpdate {
    /**
     지갑 식별키를 uuid로 변경 고려
     @GeneratedValue(strategy = GenerationType.AUTO)
     private UUID id = UUID.randomUUID();
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String userName;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY)
    private User user;

    public static Wallet create(User user) {
        return Wallet
            .builder()
            .userName(user.getName())
            .user(user)
            .build();
    }
    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }

    public void addUser(User user) {
        this.user = user;
    }
}
