package com.tasksprints.auction.domain.payment.repository;

import com.tasksprints.auction.domain.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
