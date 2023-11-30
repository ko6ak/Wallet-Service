package org.wallet_service.dto.request;

/**
 * Класс содержит входящие данные о транзакции.
 */
public class TransactionRequestDTO {
    private String id;
    private String operation;
    private String amount;
    private String description;
    private String token;

    public TransactionRequestDTO() {
    }

    public TransactionRequestDTO(String id, String operation, String amount, String description, String token) {
        this.id = id;
        this.operation = operation;
        this.amount = amount;
        this.description = description;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
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
