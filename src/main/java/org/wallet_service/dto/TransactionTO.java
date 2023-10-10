package org.wallet_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.wallet_service.entity.Operation;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Класс содержит первичные данные о Транзакции, полученные от пользовательского интерфейса.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionTO {
    private UUID id;
    private Operation operation;
    private BigDecimal amount;
    private String description;
    private long moneyAccountId;
}
