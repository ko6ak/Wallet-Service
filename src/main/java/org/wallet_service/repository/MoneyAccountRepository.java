package org.wallet_service.repository;

import org.wallet_service.entity.MoneyAccount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.wallet_service.util.DBConnection.CONNECTION;

/**
 * Класс отвечающий за сохранение Денежного счета Игрока в хранилище.
 */
public class MoneyAccountRepository {

    /**
     * Метод сохранения Денежного счета.
     * @param moneyAccount денежный счет.
     * @return Денежный счет.
     */
    public MoneyAccount save(MoneyAccount moneyAccount){
        String query = "INSERT INTO wallet.money_account(balance) VALUES (?)";
        try(PreparedStatement statement = CONNECTION.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setBigDecimal(1, moneyAccount.getBalance());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    moneyAccount.setId(generatedKeys.getInt(1));
                }
                else {
                    CONNECTION.rollback();
                    throw new SQLException("Не получилось сохранить счет");
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
        return moneyAccount;
    }

    /**
     * Метод получает счет по его id.
     * @param id идентификатор счета.
     * @return Денежный счет.
     */
    public MoneyAccount get(long id){
        MoneyAccount moneyAccount = null;
        String query = "SELECT * FROM wallet.money_account WHERE id = ?";
        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){
            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    moneyAccount = new MoneyAccount(result.getBigDecimal("balance"));
                    moneyAccount.setId(result.getInt("id"));
                }
                else {
                    CONNECTION.rollback();
                    throw new SQLException("Нет счета с таким id");
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
        return moneyAccount;
    }
}
