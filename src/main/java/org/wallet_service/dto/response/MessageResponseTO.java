package org.wallet_service.dto.response;

public class MessageResponseTO {
    private final String message;

    public MessageResponseTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
