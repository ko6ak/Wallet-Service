package org.wallet_service.controller;

import org.wallet_service.dto.TransactionTO;
import org.wallet_service.entity.Action;
import org.wallet_service.entity.Player;
import org.wallet_service.entity.Transaction;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.Beans;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Класс отвечает за обслуживание Транзакций.
 */
public class TransactionController {
    private final TransactionService transactionService = Beans.getTransactionService();
    private final PlayerActionService playerActionService = Beans.getPlayerActionService();
    public static final String REGISTER_SUCCESS = "Зарегистрировано";

    /**
     * Метод регистрирует транзакции в системе.
     * @param transactionTO содержит первичные данные о Транзакции, полученные от пользовательского интерфейса.
     * @param player текущий Игрок.
     * @return Сообщение об успешной регистрации.
     * @throws TransactionException если нет id транзакции или не уникальный id транзакции.
     */
    public String register(TransactionTO transactionTO, Player player){
        Transaction transaction;
        UUID id = transactionTO.getId();
        if (id == null) throw new TransactionException("Нет id транзакции");
        if (!transactionService.isFound(id)) {
            transaction = transactionService.save(new Transaction(transactionTO.getId(), LocalDateTime.now(), transactionTO.getDescription(),
                    transactionTO.getOperation(), transactionTO.getAmount(), transactionTO.getMoneyAccountId(), false));
        }
        else throw new TransactionException("Не уникальный id транзакции");
        BigDecimal amount = transaction.getAmount();
        playerActionService.add(player.getId(), new Action(LocalDateTime.now(),
                "Создана транзакция с типом операции " + transaction.getOperation() +
                ", суммой " + (amount.toString().contains(".") ? amount : amount + ".00") +
                " и комментарием '" + transaction.getDescription() + "'"));
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
