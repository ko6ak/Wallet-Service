package org.wallet_service.dto;

import org.wallet_service.entity.MoneyAccount;

/**
 * Класс содержит ответные данные об Игроке.
 */
public class PlayerResponseTO {
    private long id;
    private String name;
    private String email;
    private String token;
    private MoneyAccount moneyAccount;

    public PlayerResponseTO(long id, String name, String email, MoneyAccount moneyAccount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.moneyAccount = moneyAccount;
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

    public MoneyAccount getMoneyAccount() {
        return moneyAccount;
    }

    public void setMoneyAccount(MoneyAccount moneyAccount) {
        this.moneyAccount = moneyAccount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "PlayerResponseTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", moneyAccount=" + moneyAccount +
                '}';
    }
}
