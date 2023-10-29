package org.wallet_service.dto.response;

import java.sql.Timestamp;

/**
 * Класс содержит, возвращаемую из приложения, информацию о действии Игрока или информацию о транзакции для счета Игрока.
 */
public class ActionResponseDTO {
    private Timestamp dateTime;
    private String message;

    public ActionResponseDTO(Timestamp dateTime, String message) {
        this.dateTime = dateTime;
        this.message = message;
    }

    public ActionResponseDTO() {
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
