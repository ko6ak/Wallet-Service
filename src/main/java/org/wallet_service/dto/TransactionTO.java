package org.wallet_service.dto;

import jakarta.validation.constraints.*;
import org.wallet_service.entity.Operation;

import java.util.UUID;

/**
 * Класс содержит первичные данные о Транзакции, полученные от пользовательского интерфейса.
 */
public class TransactionTO {

    @NotNull
    private UUID id;

    @NotNull
    private Operation operation;

    @Pattern(regexp = "\\d+\\.\\d{2}", message = "должно соответствовать формату 0.00")
    @DecimalMin("0.01")
    private String amount;

    @NotBlank
    private String description;

    @NotBlank
    private String token;

    public TransactionTO(UUID id, Operation operation, String amount, String description, String token) {
        this.id = id;
        this.operation = operation;
        this.amount = amount;
        this.description = description;
        this.token = token;
    }

    public TransactionTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
