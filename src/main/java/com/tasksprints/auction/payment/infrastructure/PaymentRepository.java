package com.tasksprints.auction.payment.infrastructure;

import com.tasksprints.auction.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
