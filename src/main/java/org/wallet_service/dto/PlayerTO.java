package org.wallet_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс содержит первичные данные об Игроке, полученные от пользовательского интерфейса.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerTO {
    private String name;
    private String login;
    private String password;
}
