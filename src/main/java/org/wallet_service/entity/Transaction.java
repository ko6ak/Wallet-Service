package org.wallet_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс содержит данные о Транзакции.
 */
public class Transaction {
    private UUID id;
    private LocalDateTime dateTime;
    private String description;
    private OperationType operationType;
    private BigDecimal amount;
    private long moneyAccountId;
    private boolean isProcessed;

    public Transaction(UUID id, LocalDateTime dateTime, String description, OperationType operationType, BigDecimal amount, long moneyAccountId, boolean isProcessed) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.operationType = operationType;
        this.amount = amount;
        this.moneyAccountId = moneyAccountId;
        this.isProcessed = isProcessed;
    }

    public Transaction() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OperationType getOperation() {
        return operationType;
    }

    public void setOperation(OperationType operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getMoneyAccountId() {
        return moneyAccountId;
    }

    public void setMoneyAccountId(long moneyAccountId) {
        this.moneyAccountId = moneyAccountId;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", operation=" + operationType +
                ", amount=" + amount +
                ", moneyAccountId=" + moneyAccountId +
                ", isProcessed=" + isProcessed +
                '}';
    }
}
