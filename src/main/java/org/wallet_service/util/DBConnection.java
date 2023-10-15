package org.wallet_service.util;

import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CommandExecutionException;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DBConnection {
    private final static String DB_DRIVER_CLASS = ConfigParser.driver;
    private final static String DB_URL = ConfigParser.url;
    private final static String DB_USERNAME = ConfigParser.username;
    private final static String DB_PASSWORD = ConfigParser.password;

    public final static Connection CONNECTION = createConnection();

    private static Connection createConnection() {
        Connection connection = null;
        try{
            Class.forName(DB_DRIVER_CLASS);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            connection.setAutoCommit(false);
        }
        catch (ClassNotFoundException | SQLException e){
            close();
            e.printStackTrace();
        }
        return connection;
    }

    public static void close() {
        try {
            if (CONNECTION != null) CONNECTION.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
