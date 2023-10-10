package org.wallet_service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс содержит данные о Денежном счете Игрока.
 */
@Getter
@Setter
@ToString
public class MoneyAccount {
    private long id;
    private BigDecimal balance;
    private long playerId;
    private List<Action> log;

    /**
     * Создает объект Денежного счета.
     * @param balance баланс
     * @param log лог выполненных транзакций
     */
    public MoneyAccount(BigDecimal balance, List<Action> log) {
        this.balance = balance;
        this.log = log;
    }
}
