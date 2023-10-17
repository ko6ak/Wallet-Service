package org.wallet_service;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wallet_service.util.ConfigParser;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.wallet_service.util.DBConnection.CONNECTION;

@Testcontainers
public abstract class AbstractServiceTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withInitScript("db/data.sql");

    static {
        postgresContainer.start();
    }

//    @Container
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withInitScript("db/data.sql").withReuse(true);
////            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 1));;
//
    @BeforeAll
    public static void setUp() throws SQLException {
        ConfigParser.url = postgresContainer.getJdbcUrl();
        ConfigParser.username = postgresContainer.getUsername();
        ConfigParser.password = postgresContainer.getPassword();
        ConfigParser.driver = postgresContainer.getDriverClassName();
        CONNECTION = DriverManager.getConnection(ConfigParser.url, ConfigParser.username, ConfigParser.password);
        CONNECTION.setAutoCommit(false);
    }
}
