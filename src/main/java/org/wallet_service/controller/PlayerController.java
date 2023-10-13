package org.wallet_service.controller;

import org.wallet_service.dto.PlayerTO;
import org.wallet_service.entity.Action;
import org.wallet_service.entity.MoneyAccount;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.MessageException;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.service.PlayerService;
import org.wallet_service.util.Beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс отвечает за обслуживание объекта Player.
 */
public class PlayerController {
    private final PlayerService playerService = Beans.getPlayerService();
    private final MoneyAccountService moneyAccountService = Beans.getMoneyAccountService();
    private final PlayerActionService playerActionService = Beans.getPlayerActionService();
    public static final String REGISTRATION_SUCCESS = "Регистрация завершена успешно. Для работы в системе нужно войти";
    private Player currentPlayer;

    /**
     * Регистрация Player.
     * Метод регистрирует Игрока в системе и создает для него Денежный счет (MoneyAccount). Метод выводит в консоль id Игрока и id его Денежного счета.
     * @param playerTO содержит первичные данные об Игроке, полученные от пользовательского интерфейса.
     * @return Сообщение об успешной регистрации.
     * @throws AuthenticationException если Игрок с таким логином уже есть в системе.
     */
    public String registration(PlayerTO playerTO) {
        if (!playerService.isFound(playerTO.getLogin())) {
            MoneyAccount moneyAccount = moneyAccountService.save(new MoneyAccount(new BigDecimal("0.00"), new ArrayList<>()));
            Player player = playerService.save(new Player(playerTO.getName(), playerTO.getLogin(), playerTO.getPassword(), moneyAccount));
//            moneyAccount.setPlayerId(player.getId());
            playerActionService.add(player.getId(), new Action(LocalDateTime.now(), "Успешная регистрация"));
            System.out.printf("Ваш id: %s%n", player.getId());
            return REGISTRATION_SUCCESS;
        }
        throw new AuthenticationException("Игрок с таким логином уже есть в системе");
    }

    /**
     * Метод позволяет Игроку залогинится в системе.
     * @param login логин Игрока.
     * @param password пароль Игрока.
     * @return объект Игрока.
     * @throws AuthenticationException если Игрок с таким логином уже залогинен, если такого логина нет в системе или введен неправильный пароль.
     */
    public Player login(String login, String password){
        if (currentPlayer == null) {
            if (!playerService.isFound(login)) throw new AuthenticationException("Игрок с таким логином не найден");
            Optional<Player> player = playerService.get(login, password);
            if (player.isEmpty()) throw new AuthenticationException("Неправильный пароль");
            currentPlayer = player.get();
            playerActionService.add(currentPlayer.getId(), new Action(LocalDateTime.now(), "Успешный вход"));
            System.out.println("Драсте!");
            return currentPlayer;
        }
        else throw new AuthenticationException("Вы уже залогинены");
    }

    /**
     * Метод выхода из системы.
     * @throws AuthenticationException если Игрок не залогинен.
     */
    public void logout(){
        if (currentPlayer != null) {
            playerActionService.add(currentPlayer.getId(), new Action(LocalDateTime.now(), "Успешный выход"));
            currentPlayer = null;
            System.out.println("Пока!");
        }
        else throw new AuthenticationException("Вы не залогинены");
    }

    /**
     * Получение Игрока по его id.
     * @param id идентификатор Игрока.
     * @return Возвращает игрока или null если Игрок не найден.
     */
    public Player get(long id){
        return playerService.get(id);
    }

    /**
     * Метод возвращает баланс залогиненного игрока.
     * @return баланс залогиненного игрока.
     * @throws AuthenticationException если Игрок не залогинен.
     */
    public String getBalance(){
        if (currentPlayer != null) {
            playerActionService.add(currentPlayer.getId(), new Action(LocalDateTime.now(), "Вывод информации о балансе"));
            return "Баланс вашего счета: " + currentPlayer.getMoneyAccount().getBalance();
        }
        else throw new AuthenticationException("Вы не залогинены");
    }

    /**
     * Метод позволяет получить лог транзакций залогиненного Игрока.
     * @return Лог транзакций.
     * @throws AuthenticationException если Игрок не залогинен.
     */
    public List<Action> getTransactionLog(){
        if (currentPlayer == null) throw new AuthenticationException("Сначала залогинтесь");
        playerActionService.add(currentPlayer.getId(), new Action(LocalDateTime.now(), "Вывод лога транзакций"));
//        return currentPlayer.getMoneyAccount().getLog();
        return null;
    }

    /**
     * Метод позволяет получить полный лог действий Игрока, включая транзакции, по его id, вход Игрока в систему не требуется.
     * @param playerId идентификатор Игрока.
     * @return Лог действий Игрока, включая транзакции.
     * @throws MessageException если Игрок с таким id не найден.
     */
    public List<Action> getFullLog(long playerId){
        if (get(playerId) == null) throw new MessageException("Игрок с таким id не найден");
        return playerActionService.get(playerId);
    }
}
