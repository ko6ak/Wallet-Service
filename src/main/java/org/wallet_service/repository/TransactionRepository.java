package org.wallet_service.repository;

import org.wallet_service.entity.Operation;
import org.wallet_service.entity.Transaction;

import java.sql.*;
import java.util.*;

import static org.wallet_service.util.DBConnection.CONNECTION;

/**
 * Класс отвечающий за сохранение Транзакций в хранилище.
 */
public class TransactionRepository {

    /**
     * Проверяет существование Транзакции в хранилище по его id.
     * @param id идентификатор транзакции.
     * @return true/false в зависимости от наличия транзакции в хранилище.
     */
    public boolean isFound(UUID id){
        boolean isFound = false;
        String query = "SELECT EXISTS (SELECT * FROM wallet.transaction WHERE id = ?)";
        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){
            statement.setString(1, id.toString());
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    isFound = result.getBoolean("exists");
                }
                else {
                    CONNECTION.rollback();
                    throw new SQLException("Нет транзакции с таким id");
                }
            }
            CONNECTION.commit();
        }
        catch (SQLException e) {
            try{
                CONNECTION.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return isFound;
    }

    /**
     * Метод возвращает транзакцию по ее UUID.
     * @param id UUID транзакции.
     * @return транзакцию или null.
     */
    public Transaction get(UUID id){
        Transaction transaction = null;
        String query = "SELECT * FROM wallet.transaction WHERE id = ?";
        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){
            statement.setString(1, id.toString());
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    transaction = new Transaction(id,
                            Timestamp.valueOf(result.getString("date_time")).toLocalDateTime(),
                            result.getString("description"),
                            Operation.valueOf(result.getString("operation")),
                            result.getBigDecimal("amount"),
                            result.getLong("money_account_id"),
                            result.getBoolean("is_processed"));
                }
                else {
                    CONNECTION.rollback();
                    throw new SQLException("Нет транзакции с таким id");
                }
            }
            CONNECTION.commit();
        }
        catch (SQLException e) {
            try{
                CONNECTION.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return transaction;
    }

    /**
     * Сохраниение транзакции в хранилище.
     * @param transaction транзакция для сохранения.
     */
    public Transaction save(Transaction transaction){
        String query = "INSERT INTO wallet.transaction(id, date_time, description, operation, amount, money_account_id, is_processed) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement statement = CONNECTION.prepareStatement(query)) {
            statement.setString(1, transaction.getId().toString());
            statement.setTimestamp(2, Timestamp.valueOf(transaction.getDateTime()));
            statement.setString(3, transaction.getDescription());
            statement.setString(4, transaction.getOperation().toString());
            statement.setBigDecimal(5, transaction.getAmount());
            statement.setLong(6, transaction.getMoneyAccountId());
            statement.setBoolean(7, transaction.isProcessed());

            if (statement.executeUpdate() <= 0) {
                CONNECTION.rollback();
                throw new SQLException("Не получилось сохранить транзакцию");
            }

            CONNECTION.commit();
        }
        catch (SQLException e) {
            try{
                CONNECTION.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return transaction;
    }

    public void updateProcessed(Transaction transaction){
        String query = "UPDATE wallet.transaction SET is_processed = TRUE WHERE id = ?";

        try(PreparedStatement statement = CONNECTION.prepareStatement(query)) {
            statement.setString(1, transaction.getId().toString());

            if (statement.executeUpdate() <= 0) {
                CONNECTION.rollback();
                throw new SQLException("Не получилось изменить транзакцию");
            }

            CONNECTION.commit();
        }
        catch (SQLException e) {
            try{
                CONNECTION.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * Метод для получения зарегистрированных, но не обработанных транзакций.
     * @return Список необработанных транзакций.
     */
    public List<Transaction> getNotProcessed(){
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM wallet.transaction WHERE is_processed = false";
        try(Statement statement = CONNECTION.createStatement()){
            try (ResultSet result = statement.executeQuery(query)) {
                while (result.next()) {
                    transactions.add(
                            new Transaction(
                                    UUID.fromString(result.getString("id")),
                                    Timestamp.valueOf(result.getString("date_time")).toLocalDateTime(),
                                    result.getString("description"),
                                    Operation.valueOf(result.getString("operation")),
                                    result.getBigDecimal("amount"),
                                    result.getLong("money_account_id"),
                                    false));
                }
            }
            catch (SQLException e) {
                CONNECTION.rollback();
                throw new SQLException("Не удалось получить список не выполненных транзакций");
            }
            CONNECTION.commit();
        }
        catch (SQLException e) {
            try{
                CONNECTION.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return transactions;
    }
}
