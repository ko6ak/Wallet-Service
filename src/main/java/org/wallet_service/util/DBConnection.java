package org.wallet_service.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс создает подключение к БД.
 */
public final class DBConnection {
    private final static String DB_DRIVER_CLASS = ConfigParser.driver;
    private final static String DB_URL = ConfigParser.url;
    private final static String DB_USERNAME = ConfigParser.username;
    private final static String DB_PASSWORD = ConfigParser.password;

    private DBConnection() {
    }

    /**
     * Созданное подключение.
     */
    public static Connection CONNECTION = createConnection();

    /**
     * Метод, в котором создается подключение.
     * @return созданное подключение к БД.
     */
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

    /**
     * Метод закрывающий подключение к БД.
     */
    public static void close() {
        try {
            if (CONNECTION != null) CONNECTION.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
