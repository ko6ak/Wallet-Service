package org.wallet_service.repository;

import org.springframework.stereotype.Repository;
import org.wallet_service.entity.MoneyAccount;

import java.sql.*;

/**
 * Класс отвечающий за сохранение Денежного счета Игрока в хранилище.
 */
@Repository
public class MoneyAccountRepository {
    private static final String FAILED_TO_SAVE_MONEY_ACCOUNT = "Не получилось сохранить счет";
    private static final String FAILED_TO_UPDATE_BALANCE = "Не получилось обновить баланс";
    private static final String NO_MONEY_ACCOUNT_WITH_THIS_ID = "Нет счета с таким id";

    private final Connection connection;

    public MoneyAccountRepository(Connection connection) {
        this.connection = connection;
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
                    throw new SQLException(FAILED_TO_SAVE_MONEY_ACCOUNT);
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
                throw new SQLException(FAILED_TO_UPDATE_BALANCE);
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
                    throw new SQLException(NO_MONEY_ACCOUNT_WITH_THIS_ID);
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
