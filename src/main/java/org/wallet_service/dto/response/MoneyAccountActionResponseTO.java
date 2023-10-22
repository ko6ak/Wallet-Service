package org.wallet_service.dto.response;

import java.sql.Timestamp;

public class MoneyAccountActionResponseTO extends ActionResponseTO{
    private long moneyAccountId;

    public MoneyAccountActionResponseTO(Timestamp dateTime, String message, long moneyAccountId) {
        super(dateTime, message);
        this.moneyAccountId = moneyAccountId;
    }

    public long getMoneyAccountId() {
        return moneyAccountId;
    }

    public void setMoneyAccountId(long moneyAccountId) {
        this.moneyAccountId = moneyAccountId;
    }
}
