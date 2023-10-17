package org.wallet_service.service;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.wallet_service.util.ConfigParser;
import org.wallet_service.util.DBConnection;

import java.sql.Connection;

public class AbstractClass {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withInitScript("db/data.sql").withReuse(true);

        postgresContainer.start();
        ConfigParser.url = postgresContainer.getJdbcUrl();
        ConfigParser.username = postgresContainer.getUsername();
        ConfigParser.password = postgresContainer.getPassword();
        ConfigParser.driver = postgresContainer.getDriverClassName();
    }

//    @Container
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withInitScript("db/data.sql").withReuse(true);
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
}
