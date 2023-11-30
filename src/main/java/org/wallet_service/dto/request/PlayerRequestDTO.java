package org.wallet_service.dto.request;

/**
 * Класс содержит входящие данные об Игроке.
 */
public class PlayerRequestDTO {
    private String name;
    private String email;
    private String password;

    public PlayerRequestDTO() {
    }

    public PlayerRequestDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
