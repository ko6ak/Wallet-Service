package org.wallet_service.config;

import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

/**
 * Beans for Tests
 */
@Configuration
@ComponentScan("org.wallet_service")
@Profile("test")
public class TestApplicationConfig extends SpringConfig {

    @Bean
    public PostgreSQLContainer<?> postgresContainer(){
        PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName(databaseName)
                .withUsername(username)
                .withPassword(password);
        return postgresContainer;
    }

    @Bean(name = "dataSource")
    public DataSource dataSourceForTests(PostgreSQLContainer<?> postgresContainer) {
        postgresContainer.start();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl("jdbc:postgresql://localhost:" + postgresContainer.getFirstMappedPort() + "/" + databaseName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        createLiquibaseSchema(dataSource);

        return dataSource;
    }
}
