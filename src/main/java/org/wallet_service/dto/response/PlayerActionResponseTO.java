package org.wallet_service.dto.response;

import java.sql.Timestamp;

public class PlayerActionResponseTO extends ActionResponseTO{
    private long playerId;

    public PlayerActionResponseTO(Timestamp dateTime, String message, long playerId) {
        super(dateTime, message);
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
