package org.wallet_service.repository;

import org.wallet_service.entity.Action;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.wallet_service.util.DBConnection.CONNECTION;

/**
 * Класс отвечающий за сохранение информации о совершенной транзакции для Денежного счета Игрока в хранилище.
 */
public class MoneyAccountActionRepository {

    /**
     * Добавление информации о совершенной транзакции для Денежного счета.
     * @param action событие.
     */
    public void add(Action action){
        String query = "INSERT INTO wallet.money_account_actions(player_id, date_time, message) VALUES (?, ?, ?)";
        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){
            statement.setLong(1, action.getPlayer_id());
            statement.setTimestamp(2, Timestamp.valueOf(action.getDateTime()));
            statement.setString(3, action.getMessage());
            if (statement.executeUpdate() <= 0) {
                CONNECTION.rollback();
                throw new SQLException("Не получилось сохранить событие в лог транзакций");
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
     * Получение совершенных транзакций для Денежного счета с указанным идентификатором.
     * @param money_account_id идентификатор Денежного счета.
     * @return Список транзакций.
     */
    public List<Action> get(long money_account_id){
        List<Action> actions = new ArrayList<>();
        String query = "SELECT * FROM wallet.money_account_actions WHERE player_id = ?";
        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){
            statement.setLong(1, money_account_id);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    actions.add(new Action(result.getInt("id"),
                            money_account_id,
                            Timestamp.valueOf(result.getString("date_time")).toLocalDateTime(),
                            result.getString("message")));
                }
            }
            catch (SQLException e) {
                CONNECTION.rollback();
                throw new SQLException("Не удалось получить список транзакций");
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
        return actions;
    }
}
