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
@AllArgsConstructor
@ToString
public class Action {
    private LocalDateTime dateTime;
    private String message;
}
