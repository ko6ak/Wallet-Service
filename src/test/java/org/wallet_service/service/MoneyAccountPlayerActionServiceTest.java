package org.wallet_service.service;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wallet_service.PlayerTestData;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;

import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.DBConnection.CONNECTION;

@Testcontainers
public class MoneyAccountPlayerActionServiceTest extends AbstractClass{
    private static final MoneyAccountActionService moneyAccountActionService = Beans.getMoneyAccountActionService();

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
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.money_account_actions WHERE id = 3");
        assertThat(moneyAccountActionService.get(1001)).usingRecursiveComparison().isEqualTo(PlayerTestData.MONEY_ACCOUNT_ACTIONS);
    }

    @Test
    void add(){
        moneyAccountActionService.add(PlayerTestData.MONEY_ACCOUNT_ACTION_WITH_ID_3);
        assertThat(moneyAccountActionService.get(1001)).containsAll(PlayerTestData.MONEY_ACCOUNT_ACTIONS_FULL).hasSize(3);
    }
}
