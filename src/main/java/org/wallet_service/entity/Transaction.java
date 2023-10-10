package org.wallet_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс содержит данные о Транзакции.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Transaction {
    private UUID id;
    private LocalDateTime dateTime;
    private String description;
    private Operation operation;
    private BigDecimal amount;
    private long moneyAccountId;
    private boolean isProcessed;
}
