package com.tasksprints.auction.domain.payment.dto.response;

import com.tasksprints.auction.domain.payment.dto.request.TransactionRequest;
import com.tasksprints.auction.domain.payment.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionResponse {

    private Long id;
    private String transactionKey;
    private String paymentKey;
    private String orderId;
    private String payType;
    private String payStatus;
    private BigDecimal amount;


    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.transactionKey = transaction.getTransactionKey();
        this.paymentKey = transaction.getPaymentKey();
        this.orderId = transaction.getOrderId();
        this.payType = transaction.getPayType().name();
        this.payStatus = transaction.getPayStatus().name();
        this.amount = transaction.getAmount();
    }


    // Entity -> DTO
    public static TransactionResponse of(Transaction transaction) {
        return new TransactionResponse(transaction);
    }

}
