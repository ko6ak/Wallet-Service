package org.wallet_service.controller;

import org.wallet_service.aspect.Time;
import org.wallet_service.dto.PlayerTO;
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
     * Регистрация Player.
     * Метод регистрирует Игрока в системе и создает для него Денежный счет (MoneyAccount). Метод выводит в консоль id Игрока и id его Денежного счета.
     * @param playerTO содержит первичные данные об Игроке, полученные от пользовательского интерфейса.
     * @return Сообщение об успешной регистрации.
     * @throws AuthenticationException если Игрок с таким логином уже есть в системе.
     */
    public Player registration(PlayerTO playerTO) {
        if (!playerService.isFound(playerTO.getEmail())) {
            MoneyAccount moneyAccount = moneyAccountService.save(new MoneyAccount(new BigDecimal("0.00")));
            return playerService.save(new Player(playerTO.getName(), playerTO.getEmail(), playerTO.getPassword(), moneyAccount));
        }
        throw new AuthenticationException("Игрок с таким email уже есть в системе");
    }

    /**
     * Метод позволяет Игроку залогинится в системе.
     * @param email логин Игрока.
     * @param password пароль Игрока.
     * @return объект Игрока.
     * @throws AuthenticationException если Игрок с таким логином уже залогинен, если такого логина нет в системе или введен неправильный пароль.
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
     * @return баланс залогиненного игрока.
     * @throws AuthenticationException если Игрок не залогинен.
     */
    public String getBalance(String token){
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer != null && token.equals(getToken())) {
            if (JWT.isValid(token)) return playerService.get(currentPlayer.getId()).getMoneyAccount().getBalance().toString();
            else {
                setCurrentPlayer(null);
                setToken(null);
                throw new AuthenticationException("Токен просрочен, залогинтесь заново");
            }
        }
        else throw new AuthenticationException("Вы не залогинены");
    }

    /**
     * Метод позволяет получить лог транзакций залогиненного Игрока.
     * @return Лог транзакций.
     * @throws AuthenticationException если Игрок не залогинен.
     */
    public List<Action> getTransactionLog(String token){
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer != null && token.equals(getToken())) {
            if (JWT.isValid(token)) return moneyAccountActionService.get(currentPlayer.getMoneyAccount().getId());
            else {
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
