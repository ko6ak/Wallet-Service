package org.wallet_service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wallet_service.config.MainWebAppInitializer;
import org.wallet_service.config.SpringConfig;
import org.wallet_service.config.WebConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
@WebAppConfiguration
@ContextConfiguration(classes = {TestApplicationConfig.class, WebConfig.class})
@ExtendWith(SpringExtension.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class AbstractServiceTest {
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuQGdtYWlsLmNvbSIsImlhdCI6MTY5ODA4MDU5OSwiZXhwIjoxNzA2NzIwNTk5fQ.gbBa2D1WpLadT-bFhBLINj3aeG3VdApZfs-JiYEvFSs";
    public static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuQGdtYWlsLmNvbSIsImlhdCI6MTY5ODA4MDcxNywiZXhwIjoxNjk4MDgwNzc3fQ.2DXM5Cdb-yw6xFJJIOBfeOz3tUjl8oMM0lXQLEn5M4s";
    public static final String OTHER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuQGdtYWlsLmNvbSIsImlhdCI6MTY5ODA2OTc3MCwiZXhwIjoxNjk4MTU2MTcwfQ.luMAXp2CUiV9bzszEKNGcRYdcs3R3YXO9bwDUOHd3MI";

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withInitScript("db/data.sql");

    static {
        postgresContainer.start();
    }

    @Autowired
    Connection connection;
//    @Autowired
//    public AbstractServiceTest(Connection connection) {
//        this.connection = connection;
//    }

    //    @BeforeAll
//    public static void setUp() throws SQLException {
//        PropertiesParser.url = postgresContainer.getJdbcUrl();
//        PropertiesParser.username = postgresContainer.getUsername();
//        PropertiesParser.password = postgresContainer.getPassword();
//        PropertiesParser.driver = postgresContainer.getDriverClassName();
//        CONNECTION = DriverManager.getConnection(PropertiesParser.url, PropertiesParser.username, PropertiesParser.password);
//        CONNECTION.setAutoCommit(false);
//    }
}
