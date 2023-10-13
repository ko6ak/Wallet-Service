package org.wallet_service.repository;

import org.wallet_service.entity.MoneyAccount;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.wallet_service.repository.DBConnection.CONNECTION;

/**
 * Класс отвечающий за сохранение Денежного счета Игрока в хранилище.
 */
public class MoneyAccountRepository {
    private static long id = 1000;
    private final Map<Long, MoneyAccount> moneyAccounts = new HashMap<>();

    /**
     * Метод сохранения Денежного счета.
     * @param moneyAccount денежный счет.
     * @return Денежный счет.
     */
//    public MoneyAccount save(MoneyAccount moneyAccount){
//        long id = getId();
//        moneyAccount.setId(id);
//        moneyAccounts.put(id, moneyAccount);
//        return moneyAccount;
//    }

    public MoneyAccount save(MoneyAccount moneyAccount){
        long id = 0;
        String query = "INSERT INTO money_account(balance) VALUES (?)";
        try(PreparedStatement statement = CONNECTION.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            statement.setBigDecimal(1, moneyAccount.getBalance());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
                else {
                    CONNECTION.rollback();
                    throw new SQLException("Не получилось сохранить счет Игрока.");
                }
            }
            CONNECTION.commit();
        }
        catch (SQLException e) { e.printStackTrace();}
        moneyAccount.setId(id);
        return moneyAccount;
    }

    /**
     * Метод генерирует идентификатор для счета. Начальное значение для идентификатора - 1000.
     * @return id счета
     */
    private static long getId(){
        return ++id;
    }

    /**
     * Метод получает счет по его id.
     * @param id идентификатор счета.
     * @return Денежный счет.
     */
    public MoneyAccount get(long id){
        return moneyAccounts.get(id);
    }

    /**
     * Удаляет все содержимое из хранилища и возвращает генерируемое значение к начальному значению.
     * Используется только для тестовых классов.
     */
    public void clear(){
        moneyAccounts.clear();
        id = 1000;
    }
}
