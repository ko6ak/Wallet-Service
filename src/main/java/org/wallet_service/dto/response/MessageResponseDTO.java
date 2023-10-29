package org.wallet_service.dto.response;

/**
 * Класс содержит, возвращаемое из приложения, сообщение.
 */
public class MessageResponseDTO {
    private final String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
