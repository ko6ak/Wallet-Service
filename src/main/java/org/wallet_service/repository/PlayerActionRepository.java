package org.wallet_service.repository;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;
import org.wallet_service.entity.Action;
import org.wallet_service.entity.PlayerAction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс отвечающий за сохранение событий активности Игрока в хранилище.
 */
@Repository
public class PlayerActionRepository {
    private final Connection connection;

    public PlayerActionRepository(Connection connection) {
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
     * Добавление события в список активности для каждого Игрока.
     * @param playerAction событие.
     */
    public void add(PlayerAction playerAction){
        String query = "INSERT INTO wallet.player_actions(player_id, date_time, message) VALUES (?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(query)){

            statement.setLong(1, playerAction.getPlayerId());
            statement.setTimestamp(2, playerAction.getDateTime());
            statement.setString(3, playerAction.getMessage());

            if (statement.executeUpdate() <= 0) {
                connection.rollback();
                throw new SQLException("Не получилось сохранить событие в лог Игрока");
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
     * Получение списка событий для Игрока с указанным идентификатором.
     * @param playerId идентификатор Игрока.
     * @return Список событий Игрока.
     */
    public List<Action> get(long playerId){
        List<Action> actions = new ArrayList<>();
        String query = "SELECT * FROM wallet.player_actions WHERE player_id = ?";

        try(PreparedStatement statement = connection.prepareStatement(query)){

            statement.setLong(1, playerId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    actions.add(new PlayerAction(result.getInt("id"),
                            playerId,
                            Timestamp.valueOf(result.getString("date_time")),
                            result.getString("message")));
                }
            }
            catch (SQLException e) {
                connection.rollback();
                throw new SQLException("Не удалось получить список действий Игрока");
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
