package org.wallet_service.repository;

import org.springframework.stereotype.Repository;
import org.wallet_service.entity.OperationType;
import org.wallet_service.entity.Transaction;

import java.sql.*;
import java.util.*;

/**
 * Класс отвечающий за сохранение Транзакций в хранилище.
 */
@Repository
public class TransactionRepository {
    private static final String NO_TRANSACTION_WITH_THIS_ID = "Нет транзакции с таким id";
    private static final String FAILED_TO_SAVE_TRANSACTION = "Не получилось сохранить транзакцию";
    private static final String FAILED_TO_UPDATE_TRANSACTION = "Не получилось изменить транзакцию";
    private static final String FAILED_TO_GET_TRANSACTION = "Не удалось получить список не выполненных транзакций";

    private final Connection connection;

    public TransactionRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Проверяет существование Транзакции в хранилище по его id.
     * @param id идентификатор транзакции.
     * @return true/false в зависимости от наличия транзакции в хранилище.
     */
    public boolean isFound(UUID id){
        boolean isFound = false;
        String query = "SELECT EXISTS (SELECT * FROM wallet.transaction WHERE id = ?)";

        try(PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, id.toString());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    isFound = result.getBoolean("exists");
                }
                else {
                    connection.rollback();
                    throw new SQLException(NO_TRANSACTION_WITH_THIS_ID);
                }
            }
            connection.commit();
        }
        catch (SQLException e) {
            try{
                connection.rollback();
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

        try(PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, id.toString());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    transaction = new Transaction(id,
                            Timestamp.valueOf(result.getString("date_time")),
                            result.getString("description"),
                            OperationType.valueOf(result.getString("operation_type")),
                            result.getBigDecimal("amount"),
                            result.getLong("money_account_id"),
                            result.getBoolean("is_processed"));
                }
                else {
                    connection.rollback();
                    throw new SQLException(NO_TRANSACTION_WITH_THIS_ID);
                }
            }
            connection.commit();
        }
        catch (SQLException e) {
            try{
                connection.rollback();
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
        String query = "INSERT INTO wallet.transaction(id, date_time, description, operation_type, amount, money_account_id, is_processed) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, transaction.getId().toString());
            statement.setTimestamp(2, transaction.getDateTime());
            statement.setString(3, transaction.getDescription());
            statement.setString(4, transaction.getOperation().toString());
            statement.setBigDecimal(5, transaction.getAmount());
            statement.setLong(6, transaction.getMoneyAccountId());
            statement.setBoolean(7, transaction.isProcessed());

            if (statement.executeUpdate() <= 0) {
                connection.rollback();
                throw new SQLException(FAILED_TO_SAVE_TRANSACTION);
            }

            connection.commit();
        }
        catch (SQLException e) {
            try{
                connection.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return transaction;
    }

    /**
     * Метод отмечает транзакцию как обработанную.
     * @param transaction транзакция.
     */
    public void updateProcessed(Transaction transaction){
        String query = "UPDATE wallet.transaction SET is_processed = TRUE WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, transaction.getId().toString());

            if (statement.executeUpdate() <= 0) {
                connection.rollback();
                throw new SQLException(FAILED_TO_UPDATE_TRANSACTION);
            }

            connection.commit();
        }
        catch (SQLException e) {
            try{
                connection.rollback();
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

        try(Statement statement = connection.createStatement()){
            try (ResultSet result = statement.executeQuery(query)) {
                while (result.next()) {
                    transactions.add(
                            new Transaction(
                                    UUID.fromString(result.getString("id")),
                                    Timestamp.valueOf(result.getString("date_time")),
                                    result.getString("description"),
                                    OperationType.valueOf(result.getString("operation_type")),
                                    result.getBigDecimal("amount"),
                                    result.getLong("money_account_id"),
                                    false));
                }
            }
            catch (SQLException e) {
                connection.rollback();
                throw new SQLException(FAILED_TO_GET_TRANSACTION);
            }
            connection.commit();
        }
        catch (SQLException e) {
            try{
                connection.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        transactions.sort(Comparator.comparing(Transaction::getDateTime));
        return transactions;
    }
}
