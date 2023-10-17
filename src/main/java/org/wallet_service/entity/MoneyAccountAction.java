package org.wallet_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Класс содержит данные об одном действии Игрока.
 */
@Getter
@Setter
public class MoneyAccountAction extends Action {
    private long moneyAccountId;

    public MoneyAccountAction(long moneyAccountId, LocalDateTime dateTime, String message) {
        super(dateTime, message);
        this.moneyAccountId = moneyAccountId;
    }

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
