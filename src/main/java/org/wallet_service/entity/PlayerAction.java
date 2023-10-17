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
public class PlayerAction extends Action {
    private long playerId;

    public PlayerAction(long playerId, LocalDateTime dateTime, String message) {
        super(dateTime, message);
        this.playerId = playerId;
    }

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
