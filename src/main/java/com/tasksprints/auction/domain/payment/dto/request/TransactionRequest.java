package com.tasksprints.auction.domain.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionRequest {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
