package org.wallet_service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс содержит данные о Денежном счете Игрока.
 */
@Getter
@Setter
@ToString
//@AllArgsConstructor
public class MoneyAccount {
    private long id;
    private BigDecimal balance;
    private List<Action> log;

    /**
     * Создает объект Денежного счета.
     * @param balance баланс
     * @param log лог выполненных транзакций
     */
    public MoneyAccount(BigDecimal balance) {
        this.balance = balance;
        this.log = new ArrayList<>();
    }

    public MoneyAccount(long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
        this.log = new ArrayList<>();
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
}
