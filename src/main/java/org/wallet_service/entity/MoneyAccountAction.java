package org.wallet_service.entity;

import java.sql.Timestamp;

/**
 * Класс содержит информацию о действии для одной транзакции.
 */
public class MoneyAccountAction extends Action {
    private long moneyAccountId;

    public MoneyAccountAction() {
    }

    /**
     * Создает объект события транзакции.
     * @param moneyAccountId номер счета.
     * @param dateTime время события.
     * @param message сообщение.
     */
    public MoneyAccountAction(long moneyAccountId, Timestamp dateTime, String message) {
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
    public MoneyAccountAction(long id, long moneyAccountId, Timestamp dateTime, String message) {
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

    public long getMoneyAccountId() {
        return moneyAccountId;
    }

    public void setMoneyAccountId(long moneyAccountId) {
        this.moneyAccountId = moneyAccountId;
    }
}
