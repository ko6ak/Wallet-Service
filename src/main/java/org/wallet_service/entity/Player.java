package org.wallet_service.entity;

import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Класс содержит данные об Игроке.
 */
public class Player {
    private long id;
    private String name;
    private String email;
    private String password;
    private MoneyAccount moneyAccount;

    public Player() {
    }

    /**
     * Создает объект Игрока
     * @param name имя
     * @param email логин
     * @param password пароль
     * @param moneyAccount денежный счет
     */
    public Player(String name, String email, String password, MoneyAccount moneyAccount) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.moneyAccount = moneyAccount;
    }

    public Player(long id, String name, String email, String password, MoneyAccount moneyAccount) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MoneyAccount getMoneyAccount() {
        return moneyAccount;
    }

    public void setMoneyAccount(MoneyAccount moneyAccount) {
        this.moneyAccount = moneyAccount;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", moneyAccount=" + moneyAccount +
                '}';
    }
}
