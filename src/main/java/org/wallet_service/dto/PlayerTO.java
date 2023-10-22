package org.wallet_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Класс содержит первичные данные об Игроке, полученные от пользовательского интерфейса.
 */
public class PlayerTO {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 5, max = 32)
    private String password;

    public PlayerTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public PlayerTO() {
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
}
