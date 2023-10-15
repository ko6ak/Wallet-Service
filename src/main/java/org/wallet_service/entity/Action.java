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
@ToString
@AllArgsConstructor
public class Action {
    private long id;
    private long player_id;
    private LocalDateTime dateTime;
    private String message;

    public Action(long player_id, LocalDateTime dateTime, String message) {
        this.player_id = player_id;
        this.dateTime = dateTime;
        this.message = message;
    }
}
