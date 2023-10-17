package org.wallet_service.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.wallet_service.PlayerTestData;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;
import org.wallet_service.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class PlayerActionServiceTest extends AbstractClass{
    private static final PlayerActionService playerActionService = Beans.getPlayerActionService();

//    @Container
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withInitScript("db/data.sql");
////            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1));;
//
//    @BeforeAll
//    public static void setUp() {
//        postgresContainer.start();
//        ConfigParser.url = postgresContainer.getJdbcUrl();
//        ConfigParser.username = postgresContainer.getUsername();
//        ConfigParser.password = postgresContainer.getPassword();
//        ConfigParser.driver = postgresContainer.getDriverClassName();
//    }

    @Test
    void get() throws SQLException {
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id = 6");
        assertThat(playerActionService.get(1)).hasSize(5).usingRecursiveComparison().isEqualTo(PlayerTestData.ACTIONS_1);
    }

    @Test
    void add(){
        playerActionService.add(PlayerTestData.PLAYER_ACTION_WITHOUT_ID);
        assertThat(playerActionService.get(1)).hasSize(6).contains(PlayerTestData.PLAYER_ACTION_WITH_ID);
    }
}
