package org.wallet_service.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Родительский класс для логирования действий Игрока или событий транзакций.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Action {
    long id;
    LocalDateTime dateTime;
    String message;

    /**
     * Создает объект события.
     * @param dateTime время события.
     * @param message сообщение
     */
    public Action(LocalDateTime dateTime, String message) {
        this.dateTime = dateTime;
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
