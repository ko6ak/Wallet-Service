package org.wallet_service.entity;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Родительский класс для логирования действий Игрока или событий транзакций.
 */
public abstract class Action {
    long id;
    Timestamp dateTime;
    String message;

    /**
     * Создает объект события.
     * @param dateTime время события.
     * @param message сообщение
     */
    public Action(Timestamp dateTime, String message) {
        this.dateTime = dateTime;
        this.message = message;
    }

    public Action(long id, Timestamp dateTime, String message) {
        this.id = id;
        this.dateTime = dateTime;
        this.message = message;
    }

    public Action() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return id == action.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
