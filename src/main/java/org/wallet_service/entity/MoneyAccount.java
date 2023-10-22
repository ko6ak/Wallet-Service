package org.wallet_service.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс содержит данные о Денежном счете Игрока.
 */
public class MoneyAccount {
    private long id;
    private BigDecimal balance;

    /**
     * Создает объект Денежного счета.
     * @param balance баланс
     */
    public MoneyAccount(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Создает объект Денежного счета.
     * @param id номер счета
     * @param balance баланс
     */
    public MoneyAccount(long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyAccount that = (MoneyAccount) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "MoneyAccount{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
