package org.wallet_service.controller;

import org.wallet_service.aspect.Time;
import org.wallet_service.dto.TransactionTO;
import org.wallet_service.entity.Player;
import org.wallet_service.entity.PlayerAction;
import org.wallet_service.entity.Transaction;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.Beans;
import org.wallet_service.util.CurrentPlayer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Класс отвечает за обслуживание Транзакций.
 */
@Time
public class TransactionController {
    private final TransactionService transactionService = Beans.getTransactionService();
    public static final String REGISTER_SUCCESS = "Зарегистрировано";

    /**
     * Метод регистрирует транзакции в системе.
     * @param transactionTO содержит первичные данные о Транзакции, полученные от пользовательского интерфейса.
     * @return Сообщение об успешной регистрации.
     * @throws TransactionException если нет id транзакции или не уникальный id транзакции.
     */
    public String register(TransactionTO transactionTO){
        Player currentPlayer = CurrentPlayer.getCurrentPlayer();
        if (currentPlayer == null) throw new AuthenticationException("Сначала нужно залогинится");
        UUID id = transactionTO.getId();
        if (id == null) throw new TransactionException("Нет id транзакции");
        if (!transactionService.isFound(id)) {
            transactionService.save(new Transaction(transactionTO.getId(), LocalDateTime.now(), transactionTO.getDescription(),
                    transactionTO.getOperation(), new BigDecimal(transactionTO.getAmount()), currentPlayer.getMoneyAccount().getId(), false));
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
