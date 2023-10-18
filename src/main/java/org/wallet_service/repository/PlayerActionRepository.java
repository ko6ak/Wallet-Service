package org.wallet_service.repository;

import org.wallet_service.entity.Action;
import org.wallet_service.entity.PlayerAction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.wallet_service.util.DBConnection.CONNECTION;

/**
 * Класс отвечающий за сохранение событий активности Игрока в хранилище.
 */
public class PlayerActionRepository {

    /**
     * Добавление события в список активности для каждого Игрока.
     * @param playerAction событие.
     */
    public void add(PlayerAction playerAction){
        String query = "INSERT INTO wallet.player_actions(player_id, date_time, message) VALUES (?, ?, ?)";

        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){

            statement.setLong(1, playerAction.getPlayerId());
            statement.setTimestamp(2, Timestamp.valueOf(playerAction.getDateTime()));
            statement.setString(3, playerAction.getMessage());

            if (statement.executeUpdate() <= 0) {
                CONNECTION.rollback();
                throw new SQLException("Не получилось сохранить событие в лог Игрока");
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
     * Получение списка событий для Игрока с указанным идентификатором.
     * @param playerId идентификатор Игрока.
     * @return Список событий Игрока.
     */
    public List<Action> get(long playerId){
        List<Action> actions = new ArrayList<>();
        String query = "SELECT * FROM wallet.player_actions WHERE player_id = ?";

        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){

            statement.setLong(1, playerId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    actions.add(new PlayerAction(result.getInt("id"),
                            playerId,
                            Timestamp.valueOf(result.getString("date_time")).toLocalDateTime(),
                            result.getString("message")));
                }
            }
            catch (SQLException e) {
                CONNECTION.rollback();
                throw new SQLException("Не удалось получить список действий Игрока");
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
