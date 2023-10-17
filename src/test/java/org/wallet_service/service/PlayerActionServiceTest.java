package org.wallet_service.service;

import org.junit.jupiter.api.Test;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.util.Beans;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class PlayerActionServiceTest extends AbstractServiceTest {
    private static final PlayerActionService playerActionService = Beans.getPlayerActionService();

    @Test
    void get() {
        assertThat(playerActionService.get(1)).hasSize(5).containsAll(PlayerTestData.ACTIONS_1);
    }

    @Test
    void add() throws SQLException {
        playerActionService.add(PlayerTestData.PLAYER_ACTION_WITHOUT_ID);
        assertThat(playerActionService.get(1)).hasSize(6).contains(PlayerTestData.PLAYER_ACTION_WITH_ID);
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id = 6");
    }
}
