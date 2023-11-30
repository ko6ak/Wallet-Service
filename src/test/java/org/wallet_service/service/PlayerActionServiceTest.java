package org.wallet_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.entity.PlayerAction;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerActionServiceTest extends AbstractServiceTest {

    @Autowired
    private PlayerActionService playerActionService;

    @BeforeEach
    void setUp() throws SQLException {
        connection.createStatement().executeUpdate("TRUNCATE TABLE wallet.player_actions; ALTER SEQUENCE wallet.player_actions_seq RESTART;");
        PlayerTestData.ACTIONS_1.forEach(p -> playerActionService.add((PlayerAction) p));
    }

    @Test
    void get() {
        assertThat(playerActionService.get(1)).hasSize(5).containsAll(PlayerTestData.ACTIONS_1);
    }

    @Test
    void add() throws SQLException {
        playerActionService.add(PlayerTestData.PLAYER_ACTION_WITHOUT_ID);
        assertThat(playerActionService.get(1)).hasSize(6).contains(PlayerTestData.PLAYER_ACTION_WITH_ID);
        connection.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id = 6");
    }
}
