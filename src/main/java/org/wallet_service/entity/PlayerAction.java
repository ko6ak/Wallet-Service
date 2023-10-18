package org.wallet_service.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс содержит данные об одном действии Игрока.
 */
@Getter
@Setter
public class PlayerAction extends Action {
    private long playerId;

    /**
     * Создает объект действия Игрока.
     * @param playerId идентификатор Игрока.
     * @param dateTime время события.
     * @param message сообщение.
     */
    public PlayerAction(long playerId, LocalDateTime dateTime, String message) {
        super(dateTime, message);
        this.playerId = playerId;
    }

    /**
     * Создает объект действия Игрока.
     * @param id идентификатор события.
     * @param playerId идентификатор Игрока.
     * @param dateTime время события.
     * @param message сообщение.
     */
    public PlayerAction(long id, long playerId, LocalDateTime dateTime, String message) {
        super(id, dateTime, message);
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", message='" + message + '\'' +
                ", playerId=" + playerId +
                '}';
    }
}
