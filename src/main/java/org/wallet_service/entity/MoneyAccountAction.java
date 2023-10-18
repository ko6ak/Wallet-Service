package org.wallet_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс содержит информацию о действии для одной транзакции.
 */
@Getter
@Setter
public class MoneyAccountAction extends Action {
    private long moneyAccountId;

    /**
     * Создает объект события транзакции.
     * @param moneyAccountId номер счета.
     * @param dateTime время события.
     * @param message сообщение.
     */
    public MoneyAccountAction(long moneyAccountId, LocalDateTime dateTime, String message) {
        super(dateTime, message);
        this.moneyAccountId = moneyAccountId;
    }

    /**
     * Создает объект события транзакции.
     * @param id идентификатор события.
     * @param moneyAccountId номер счета.
     * @param dateTime время события.
     * @param message сообщение.
     */
    public MoneyAccountAction(long id, long moneyAccountId, LocalDateTime dateTime, String message) {
        super(id, dateTime, message);
        this.moneyAccountId = moneyAccountId;
    }

    @Override
    public String toString() {
        return "MoneyAccountAction{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", message='" + message + '\'' +
                ", moneyAccountId=" + moneyAccountId +
                '}';
    }
}
