package org.wallet_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Класс содержит данные об Игроке.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Player {
    private long id;
    private String name;
    private String login;
    private String password;
    private MoneyAccount moneyAccount;

    /**
     * Создает объект Игрока
     * @param name имя
     * @param login логин
     * @param password пароль
     * @param moneyAccount денежный счет
     */
    public Player(String name, String login, String password, MoneyAccount moneyAccount) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.moneyAccount = moneyAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
