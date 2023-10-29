package org.wallet_service.entity;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Класс содержит данные об одном действии Игрока.
 */
public class PlayerAction extends Action {
    private long playerId;


    public PlayerAction() {
    }

    /**
     * Создает объект действия Игрока.
     * @param playerId идентификатор Игрока.
     * @param dateTime время события.
     * @param message сообщение.
     */
    public PlayerAction(long playerId, Timestamp dateTime, String message) {
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
    public PlayerAction(long id, long playerId, Timestamp dateTime, String message) {
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

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
