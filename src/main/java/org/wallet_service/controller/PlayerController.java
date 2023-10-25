package org.wallet_service.controller;

import org.wallet_service.aspect.Time;
import org.wallet_service.entity.*;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.MessageException;
import org.wallet_service.service.MoneyAccountActionService;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.service.PlayerService;
import org.wallet_service.util.Beans;
import org.wallet_service.util.JWT;

import java.math.BigDecimal;
import java.util.*;

import static org.wallet_service.util.CurrentPlayer.*;

/**
 * Класс отвечает за обслуживание объекта Player.
 */
@Time
public class PlayerController {
    private final PlayerService playerService = Beans.getPlayerService();
    private final MoneyAccountService moneyAccountService = Beans.getMoneyAccountService();
    private final PlayerActionService playerActionService = Beans.getPlayerActionService();
    private static final MoneyAccountActionService moneyAccountActionService = Beans.getMoneyAccountActionService();


    /**
     * Регистрация Игрока.
     * Метод регистрирует Игрока в системе и создает для него Денежный счет (MoneyAccount).
     * @param name имя Игрока.
     * @param email Почта Игрока.
     * @param password пароль Игрока.
     * @return игрока.
     * @throws AuthenticationException если Игрок с таким email уже есть в системе.
     */
    public Player registration(String name, String email, String password) {
        if (!playerService.isFound(email)) {
            MoneyAccount moneyAccount = moneyAccountService.save(new MoneyAccount(new BigDecimal("0.00")));
            return playerService.save(new Player(name, email, password, moneyAccount));
        }
        throw new AuthenticationException("Игрок с таким email уже есть в системе");
    }

    /**
     * Метод позволяет Игроку залогинится в системе. Создает токен аутентификации.
     * @param email логин Игрока.
     * @param password пароль Игрока.
     * @return объект Игрока.
     * @throws AuthenticationException если Игрок с таким email уже вошел, если такого email нет в системе или введен неправильный пароль.
     */
    public Player login(String email, String password){
        if (getCurrentPlayer() == null) {
            if (!playerService.isFound(email)) throw new AuthenticationException("Игрок с таким email не найден");
            Player player = playerService.get(email, password);
            if (player == null) throw new AuthenticationException("Неправильный пароль");
            String token = JWT.create(player);
            setToken(token);
            setCurrentPlayer(player);
            return getCurrentPlayer();
        }
        else throw new AuthenticationException("Сначала нужно выйти");
    }

    /**
     * Метод выхода из системы.
     * @param token токен вошедшего Игрока.
     * @throws AuthenticationException если Игрок не залогинен.
     */
    public String logout(String token){
        if (getCurrentPlayer() != null && token.equals(getToken())) {
            setCurrentPlayer(null);
            setToken(null);
            return "Пока!";
        }
        else throw new AuthenticationException("Вы не залогинены");
    }

    /**
     * Метод возвращает баланс залогиненного игрока.
     * @param token токен вошедшего Игрока.
     * @return баланс залогиненного игрока.
     * @throws AuthenticationException если Игрок не залогинен, токен из параметра метода не совпадает с сохраненным в системе или токен просрочен.
     */
    public String getBalance(String token){
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer != null && token.equals(getToken())) {
            try{
                JWT.validate(token);
                return playerService.get(currentPlayer.getId()).getMoneyAccount().getBalance().toString();
            }
            catch (AuthenticationException e){
                setCurrentPlayer(null);
                setToken(null);
                throw new AuthenticationException("Токен просрочен, залогинтесь заново");
            }
        }
        else throw new AuthenticationException("Вы не залогинены");
    }

    /**
     * Метод позволяет получить лог транзакций залогиненного Игрока.
     * @param token токен вошедшего Игрока.
     * @return Лог транзакций.
     * @throws AuthenticationException если Игрок не залогинен, токен из параметра метода не совпадает с сохраненным в системе или токен просрочен.
     */
    public List<Action> getTransactionLog(String token){
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer != null && token.equals(getToken())) {
            try{
                JWT.validate(token);
                return moneyAccountActionService.get(currentPlayer.getMoneyAccount().getId());
            }
            catch (AuthenticationException e){
                setCurrentPlayer(null);
                setToken(null);
                throw new AuthenticationException("Токен просрочен, залогинтесь заново");
            }
        }
        else throw new AuthenticationException("Сначала залогинтесь");
    }

    /**
     * Метод позволяет получить полный лог действий Игрока, включая транзакции, по его id, вход Игрока в систему не требуется.
     * @param playerId идентификатор Игрока.
     * @return Лог действий Игрока, включая транзакции.
     * @throws MessageException если Игрок с таким id не найден.
     */
    public List<Action> getFullLog(long playerId){
        Player player = playerService.get(playerId);
        if (player == null) throw new MessageException("Игрок с таким id не найден");
        List<Action> playerActions = moneyAccountActionService.get(player.getMoneyAccount().getId());
        playerActions.addAll(playerActionService.get(playerId));
        playerActions.sort(Comparator.comparing(Action::getDateTime));
        return playerActions;
    }
}
