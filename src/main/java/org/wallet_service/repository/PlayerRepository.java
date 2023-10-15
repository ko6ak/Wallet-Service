package org.wallet_service.repository;

import org.wallet_service.entity.Action;
import org.wallet_service.entity.MoneyAccount;
import org.wallet_service.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.wallet_service.util.DBConnection.CONNECTION;

/**
 * Класс отвечающий за сохранение Игрока в хранилище.
 */
public class PlayerRepository {

    /**
     * Получение Игрока по его логину и паролю.
     * @param login логин Игрока.
     * @param password пароль Игрока.
     * @return Игрока или null, если Игрок не найден.
     */
    public Player get(String login, String password){
        Player player = null;
        String moneyAccountActionsQuery = "SELECT * FROM wallet.money_account_actions WHERE money_account_actions.id = ?";
        String playerQuery = "SELECT * FROM wallet.player " +
                "JOIN wallet.money_account ON money_account.id = player.money_account_id WHERE login = ? AND password = ?";
        try(PreparedStatement moneyAccountActionsStatement = CONNECTION.prepareStatement(moneyAccountActionsQuery);
            PreparedStatement playerStatement = CONNECTION.prepareStatement(playerQuery)){
            playerStatement.setString(1, login);
            playerStatement.setString(2, password);

            player = buildPlayer(moneyAccountActionsStatement, playerStatement);

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
        String moneyAccountActionsQuery = "SELECT * FROM wallet.money_account_actions WHERE money_account_actions.id = ?";
        String playerQuery = "SELECT * FROM wallet.player " +
                "JOIN wallet.money_account ON money_account.id = player.money_account_id WHERE player.id = ?";
        try(PreparedStatement moneyAccountActionsStatement = CONNECTION.prepareStatement(moneyAccountActionsQuery);
            PreparedStatement playerStatement = CONNECTION.prepareStatement(playerQuery)){
            playerStatement.setLong(1, id);

            player = buildPlayer(moneyAccountActionsStatement, playerStatement);

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

    private static Player buildPlayer(PreparedStatement moneyAccountActionsStatement, PreparedStatement playerStatement) throws SQLException {
        Player player;
        MoneyAccount moneyAccount;
        long moneyAccountId;
        long playerId;
        try (ResultSet result = playerStatement.executeQuery()) {
            if (result.next()) {
                playerId = result.getLong("id");
                moneyAccountId = result.getLong("money_account_id");
                moneyAccount = new MoneyAccount(moneyAccountId,
                        result.getBigDecimal("balance"), playerId);
                player = new Player(playerId,
                        result.getString("name"),
                        result.getString("login"),
                        result.getString("password"),
                        moneyAccount);
            }
            else {
                CONNECTION.rollback();
                throw new SQLException("Неправильный логин, пароль или id");
            }
        }
        List<Action> actions = new ArrayList<>();
        moneyAccountActionsStatement.setLong(1, moneyAccountId);
        try (ResultSet result = moneyAccountActionsStatement.executeQuery()) {
            while (result.next()) {
                actions.add(new Action(result.getInt("id"),
                        playerId,
                        Timestamp.valueOf(result.getString("date_time")).toLocalDateTime(),
                        result.getString("message")));
            }
        }
        catch (SQLException e) {
            CONNECTION.rollback();
            throw new SQLException("Не удалось получить список транзакций");
        }
        moneyAccount.setLog(actions);
        return player;
    }

    /**
     * Сохраняет Игрока в хранилище.
     * @param player Игрок для сохранения.
     * @return Игрока.
     */
    public Player save(Player player){
        MoneyAccount moneyAccount = player.getMoneyAccount();
        String moneyAccountQuery = "INSERT INTO wallet.money_account(balance) VALUES (?)";
        String playerQuery = "INSERT INTO wallet.player(name, login, password, money_account_id) VALUES (?, ?, ?, ?)";

        try(PreparedStatement moneyAccountStatement = CONNECTION.prepareStatement(moneyAccountQuery, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement playerStatement = CONNECTION.prepareStatement(playerQuery, Statement.RETURN_GENERATED_KEYS)){

            moneyAccountStatement.setBigDecimal(1, player.getMoneyAccount().getBalance());
            playerStatement.setString(1, player.getName());
            playerStatement.setString(2, player.getLogin());
            playerStatement.setString(3, player.getPassword());
            playerStatement.setLong(4, player.getMoneyAccount().getId());

            moneyAccountStatement.executeUpdate();
            playerStatement.executeUpdate();

            try (ResultSet moneyAccountGeneratedKey = moneyAccountStatement.getGeneratedKeys();
                 ResultSet playerGeneratedKey = playerStatement.getGeneratedKeys()) {
                if (moneyAccountGeneratedKey.next() && playerGeneratedKey.next()) {
                    moneyAccount.setId(moneyAccountGeneratedKey.getInt("id"));
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
     * @param login логин Игрока.
     * @return true/false в зависимости от наличия Игрока в хранилище.
     */
    public boolean isFound(String login){
        boolean isFound = false;
        String query = "SELECT EXISTS (SELECT * FROM wallet.player WHERE login = ?)";
        try(PreparedStatement statement = CONNECTION.prepareStatement(query)){
            statement.setString(1, login);
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
