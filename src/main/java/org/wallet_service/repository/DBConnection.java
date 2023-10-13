package org.wallet_service.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DBConnection {
    private final static String DB_DRIVER_CLASS = "org.postgresql.Driver";
    private final static String DB_URL = "jdbc:postgresql://localhost:5432/wallet_db?serverTimezone=Europe/Moscow&useSSL=false";
    private final static String DB_USERNAME = "root";
    private final static String DB_PASSWORD = "root";

    public final static Connection CONNECTION = createConnection();

    private static Connection createConnection() {
        Connection connection = null;
        try{
            Class.forName(DB_DRIVER_CLASS);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            connection.setAutoCommit(false);
        }
        catch (ClassNotFoundException | SQLException e){
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
        finally {
            try {
                if (CONNECTION != null) CONNECTION.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
