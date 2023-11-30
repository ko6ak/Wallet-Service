package org.wallet_service.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация Spring
 */
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class SpringConfig {
    @Value("${spring.datasource.driver-class-name}")
    protected String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    protected String username;
    @Value("${spring.datasource.password}")
    protected String password;
    @Value("${spring.datasource.databaseName}")
    protected String databaseName;
    @Value("${spring.liquibase.changeLogFile}")
    private String changelogFile;
    @Value("${spring.liquibase.defaultSchemaName}")
    private String defaultSchemaName;
    @Value("${spring.liquibase.liquibaseSchemaName}")
    private String liquibaseSchemaName;

    @Bean
    @Profile("default")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        createLiquibaseSchema(dataSource);

        return dataSource;
    }

    public void createLiquibaseSchema(DataSource dataSource){
        try (Connection conn = dataSource.getConnection(); Statement statement = conn.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + liquibaseSchemaName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public Connection connection(DataSource dataSource){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return connection;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changelogFile);
        liquibase.setDataSource(dataSource());
        liquibase.setDefaultSchema(defaultSchemaName);
        liquibase.setLiquibaseSchema(liquibaseSchemaName);
//        liquibase.setDropFirst(false);
        liquibase.setShouldRun(true);

        Map<String, String> params = new HashMap<>();
        params.put("verbose", "true");
        liquibase.setChangeLogParameters(params);

        return liquibase;
    }
}
