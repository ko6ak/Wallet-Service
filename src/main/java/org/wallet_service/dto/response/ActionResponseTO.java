package org.wallet_service.dto.response;

import java.sql.Timestamp;

public class ActionResponseTO {
    private Timestamp dateTime;
    private String message;

    public ActionResponseTO(Timestamp dateTime, String message) {
        this.dateTime = dateTime;
        this.message = message;
    }

    public ActionResponseTO() {
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
