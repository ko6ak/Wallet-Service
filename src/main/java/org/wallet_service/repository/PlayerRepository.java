package org.wallet_service.repository;

import org.wallet_service.entity.*;

import java.sql.*;

import static org.wallet_service.util.DBConnection.CONNECTION;

/**
 * Класс отвечающий за сохранение Игрока в хранилище.
 */
public class PlayerRepository {

    /**
     * Получение Игрока по его логину и паролю.
     * @param email логин Игрока.
     * @param password пароль Игрока.
     * @return Игрока или null, если Игрок не найден.
     */
    public Player get(String email, String password){
        Player player = null;
        String playerQuery = "SELECT * FROM wallet.player " +
                "JOIN wallet.money_account ON money_account.id = player.money_account_id WHERE email = ? AND password = ?";

        try(PreparedStatement playerStatement = CONNECTION.prepareStatement(playerQuery)){

            playerStatement.setString(1, email);
            playerStatement.setString(2, password);

            player = buildPlayer(playerStatement);

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
        return player;
    }

    /**
     * Получение Игрока по id.
     * @param id идентификатор Игрока
     * @return Игрока или null если Игрок не найден.
     */
    public Player get(long id){
        Player player = null;
        String playerQuery = "SELECT * FROM wallet.player " +
                "JOIN wallet.money_account ON money_account.id = player.money_account_id WHERE player.id = ?";

        try(PreparedStatement playerStatement = CONNECTION.prepareStatement(playerQuery)){

            playerStatement.setLong(1, id);

            player = buildPlayer(playerStatement);

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
        return player;
    }

    /**
     * Метод создающий Игрока из данных, полученных из БД.
//     * @param moneyAccountActionsStatement Объект PreparedStatement для создания лога транзакций.
     * @param playerStatement Объект PreparedStatement для создания Игрока.
     * @return Игрока.
     * @throws SQLException Если не удалось получить список транзакций.
     */
    private static Player buildPlayer(PreparedStatement playerStatement) throws SQLException {
        Player player;
        MoneyAccount moneyAccount;
        long moneyAccountId;
        long playerId;

        try (ResultSet result = playerStatement.executeQuery()) {
            if (result.next()) {
                playerId = result.getLong("id");
                moneyAccountId = result.getLong("money_account_id");

                moneyAccount = new MoneyAccount(moneyAccountId,
                        result.getBigDecimal("balance"));

                player = new Player(playerId,
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("password"),
                        moneyAccount);
            }
            else {
                CONNECTION.rollback();
                return null;
            }
        }
        return player;
    }

    /**
     * Сохраняет Игрока в хранилище.
     * @param player Игрок для сохранения.
     * @return Игрока.
     */
    public Player save(Player player){
        String playerQuery = "INSERT INTO wallet.player(name, email, password, money_account_id) VALUES (?, ?, ?, ?)";

        try(PreparedStatement playerStatement = CONNECTION.prepareStatement(playerQuery, Statement.RETURN_GENERATED_KEYS)){

            playerStatement.setString(1, player.getName());
            playerStatement.setString(2, player.getEmail());
            playerStatement.setString(3, player.getPassword());
            playerStatement.setLong(4, player.getMoneyAccount().getId());

            playerStatement.executeUpdate();

            try (ResultSet playerGeneratedKey = playerStatement.getGeneratedKeys()) {
                if (playerGeneratedKey.next()) {
                    player.setId(playerGeneratedKey.getInt("id"));
                }
                else {
                    CONNECTION.rollback();
                    throw new SQLException("Не получилось сохранить Игрока");
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
        return player;
    }

    /**
     * Проверяет существование Игрока в хранилище по его логину.
     * @param email логин Игрока.
     * @return true/false в зависимости от наличия Игрока в хранилище.
     */
    public boolean isFound(String email){
        boolean isFound = false;
        String query = "SELECT EXISTS (SELECT * FROM wallet.player WHERE email = ?)";

        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    isFound = result.getBoolean("exists");
                }
                else {
                    CONNECTION.rollback();
                    throw new SQLException("Нет Игрока с таким логином");
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
}
