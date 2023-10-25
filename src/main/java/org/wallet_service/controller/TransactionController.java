package org.wallet_service.controller;

import org.wallet_service.aspect.Time;
import org.wallet_service.entity.Operation;
import org.wallet_service.entity.Player;
import org.wallet_service.entity.Transaction;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.Beans;
import org.wallet_service.util.CurrentPlayer;
import org.wallet_service.util.JWT;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.wallet_service.util.CurrentPlayer.*;

/**
 * Класс отвечает за обслуживание Транзакций.
 */
@Time
public class TransactionController {
    private final TransactionService transactionService = Beans.getTransactionService();
    public static final String REGISTER_SUCCESS = "Зарегистрировано";

    /**
     * Метод регистрирует транзакции в системе.
     * @param id идентификатор транзакции.
     * @param operation тип операции.
     * @param amount сумма.
     * @param description комментарий.
     * @param token токен.
     * @return Сообщение об успешной регистрации.
     * @throws TransactionException если не уникальный id транзакции.
     * @throws AuthenticationException если Игрок не залогинен, токен из параметра метода не совпадает с сохраненным в системе или токен просрочен.
     */
    public String register(UUID id, Operation operation, String amount, String description, String token){
        Player currentPlayer = CurrentPlayer.getCurrentPlayer();
        if (currentPlayer == null || !token.equals(getToken())) throw new AuthenticationException("Сначала нужно залогинится");
        try{
            JWT.validate(token);
        }
        catch (AuthenticationException e){
            setCurrentPlayer(null);
            setToken(null);
            throw new AuthenticationException("Токен просрочен, залогинтесь заново");
        }
        if (!transactionService.isFound(id)) {
            transactionService.save(new Transaction(id, LocalDateTime.now(), description,
                    operation, new BigDecimal(amount), currentPlayer.getMoneyAccount().getId(), false));
        }
        else throw new TransactionException("Не уникальный id транзакции");
        return REGISTER_SUCCESS;
    }

    /**
     * Метод позволяет получить список зарегистрированныз, но не обработанных транзакций.
     * @return Список необработанных транзакций.
     */
    public List<Transaction> getNotProcessed(){
        return transactionService.getNotProcessed();
    }
}
