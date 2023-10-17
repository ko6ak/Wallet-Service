package org.wallet_service.service;

import org.junit.jupiter.api.*;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.util.Beans;

import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class MoneyAccountPlayerActionServiceTest extends AbstractServiceTest {
    private static final MoneyAccountActionService moneyAccountActionService = Beans.getMoneyAccountActionService();

    @Test
    void get() {
        assertThat(moneyAccountActionService.get(1001)).containsAll(PlayerTestData.MONEY_ACCOUNT_ACTIONS).hasSize(2);
    }

    @Test
    void add() throws SQLException {
        moneyAccountActionService.add(PlayerTestData.MONEY_ACCOUNT_ACTION_WITH_ID_3);
        assertThat(moneyAccountActionService.get(1001)).containsAll(PlayerTestData.MONEY_ACCOUNT_ACTIONS_FULL).hasSize(3);
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.money_account_actions WHERE id = 3");
    }
}
