package org.wallet_service.repository;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;
import org.wallet_service.entity.MoneyAccount;

import java.sql.*;


/**
 * Класс отвечающий за сохранение Денежного счета Игрока в хранилище.
 */
@Repository
public class MoneyAccountRepository {
    private final Connection connection;

    public MoneyAccountRepository(Connection connection) {
        this.connection = connection;
    }

    @PreDestroy
    private void destroy(){
        try {
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод сохранения Денежного счета.
     * @param moneyAccount денежный счет.
     * @return Денежный счет.
     */
    public MoneyAccount save(MoneyAccount moneyAccount){
        String query = "INSERT INTO wallet.money_account(balance) VALUES (?)";

        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            statement.setBigDecimal(1, moneyAccount.getBalance());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    moneyAccount.setId(generatedKeys.getInt(1));
                }
                else {
                    connection.rollback();
                    throw new SQLException("Не получилось сохранить счет");
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
        return moneyAccount;
    }

    /**
     * Метод для обновления баланса счета.
     * @param moneyAccount счет.
     */
    public boolean updateBalance(MoneyAccount moneyAccount){
        String query = "UPDATE wallet.money_account SET balance = ? WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            statement.setBigDecimal(1, moneyAccount.getBalance());
            statement.setLong(2, moneyAccount.getId());
            statement.executeUpdate();

            if (statement.executeUpdate() <= 0) {
                connection.rollback();
                throw new SQLException("Не получилось обновить баланс");
            }
            connection.commit();
            return true;
        }
        catch (SQLException e) {
            try{
                connection.rollback();
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Метод получает счет по его id.
     * @param id идентификатор счета.
     * @return Денежный счет.
     */
    public MoneyAccount get(long id){
        MoneyAccount moneyAccount = null;
        String query = "SELECT * FROM wallet.money_account WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(query)){

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    moneyAccount = new MoneyAccount(result.getBigDecimal("balance"));
                    moneyAccount.setId(result.getInt("id"));
                }
                else {
                    connection.rollback();
                    throw new SQLException("Нет счета с таким id");
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
        return moneyAccount;
    }
}
