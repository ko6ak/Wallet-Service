package org.wallet_service.dto.request;

public class TokenRequestDTO {
    private String token;

    public TokenRequestDTO() {
    }

    public TokenRequestDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
