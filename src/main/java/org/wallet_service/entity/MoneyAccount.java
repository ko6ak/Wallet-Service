package org.wallet_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private long playerId;
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

    public MoneyAccount(long id, BigDecimal balance, long playerId) {
        this.id = id;
        this.balance = balance;
        this.playerId = playerId;
        this.log = new ArrayList<>();
    }
}
