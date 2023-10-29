package org.wallet_service.repository;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;
import org.wallet_service.entity.Action;
import org.wallet_service.entity.MoneyAccountAction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс отвечающий за сохранение информации о совершенной транзакции для Денежного счета Игрока в хранилище.
 */
@Repository
public class MoneyAccountActionRepository {
    private final Connection connection;

    public MoneyAccountActionRepository(Connection connection) {
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
     * Добавление информации о совершенной транзакции для Денежного счета.
     * @param moneyAccountAction событие.
     */
    public void add(MoneyAccountAction moneyAccountAction){
        String query = "INSERT INTO wallet.money_account_actions(money_account_id, date_time, message) VALUES (?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(query)){

            statement.setLong(1, moneyAccountAction.getMoneyAccountId());
            statement.setTimestamp(2, moneyAccountAction.getDateTime());
            statement.setString(3, moneyAccountAction.getMessage());

            if (statement.executeUpdate() <= 0) {
                connection.rollback();
                throw new SQLException("Не получилось сохранить событие в лог транзакций");
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
     * Получение совершенных транзакций для Денежного счета с указанным идентификатором.
     * @param moneyAccountId идентификатор Денежного счета.
     * @return Список транзакций.
     */
    public List<Action> get(long moneyAccountId){
        List<Action> actions = new ArrayList<>();
        String query = "SELECT * FROM wallet.money_account_actions WHERE money_account_id = ?";

        try(PreparedStatement statement = connection.prepareStatement(query)){

            statement.setLong(1, moneyAccountId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    actions.add(new MoneyAccountAction(result.getInt("id"),
                            moneyAccountId,
                            Timestamp.valueOf(result.getString("date_time")),
                            result.getString("message")));
                }
            }
            catch (SQLException e) {
                connection.rollback();
                throw new SQLException("Не удалось получить список транзакций");
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
        return actions;
    }
}
