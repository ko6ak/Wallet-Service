package org.wallet_service.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.entity.MoneyAccountAction;

import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyAccountActionServiceTest extends AbstractServiceTest {

    @Autowired
    private MoneyAccountActionService moneyAccountActionService;

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate("TRUNCATE TABLE wallet.money_account_actions; ALTER SEQUENCE wallet.money_account_actions_seq RESTART;");
        PlayerTestData.MONEY_ACCOUNT_ACTIONS.forEach(m -> moneyAccountActionService.add((MoneyAccountAction) m));
    }

    @Test
    void get() {
        assertThat(moneyAccountActionService.get(1001)).containsAll(PlayerTestData.MONEY_ACCOUNT_ACTIONS).hasSize(2);
    }

    @Test
    void add() throws SQLException {
        moneyAccountActionService.add(PlayerTestData.MONEY_ACCOUNT_ACTION_WITH_ID_3);
        assertThat(moneyAccountActionService.get(1001)).containsAll(PlayerTestData.MONEY_ACCOUNT_ACTIONS_FULL).hasSize(3);
        connection.createStatement().executeUpdate("DELETE FROM wallet.money_account_actions WHERE id = 3");
    }
}
