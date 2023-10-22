package org.wallet_service.util;

import org.wallet_service.entity.*;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.MoneyAccountActionService;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.controller.TransactionController;
import org.wallet_service.service.TransactionService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, отвечающий за обработку зарегистрированных транзакций.
 */
public final class Processing {
    private static final TransactionController transactionController = Beans.getTransactionController();
    private static final TransactionService transactionService = Beans.getTransactionService();
    private static final MoneyAccountService moneyAccountService = Beans.getMoneyAccountService();
    private static final MoneyAccountActionService moneyAccountActionService = Beans.getMoneyAccountActionService();

    private Processing() {
    }

    /**
     * Метод получает не обработанные транзакции и обрабатывает их в соответствии с типом операции.
     * Выводит сообщение о завершении обработки или если транзакций для обработки нет. Не прерывает работу если транзакцию выполнить не удалось.
     */
    public static String process(){
        List<Transaction> transactions = transactionController.getNotProcessed();
        if (!transactions.isEmpty()) {
            transactions.forEach(t -> {
                switch (t.getOperation()) {
                    case CREDIT -> credit(t);
                    case DEBIT -> debit(t);
                }
                transactionService.updateProcessed(t);
            });
            return "Все транзакции обработаны";
        }
        else return "Нет транзакций для обработки";
    }

    /**
     * Метод обрабатывает операцию увеличения баланса на счету.
     * @param transaction транзакция для обработки.
     */
    private static void credit(Transaction transaction) {
        MoneyAccount moneyAccount = moneyAccountService.get(transaction.getMoneyAccountId());
        moneyAccount.setBalance(moneyAccount.getBalance().add(transaction.getAmount()));
        moneyAccountService.updateBalance(moneyAccount);
    }

    /**
     * Метод обрабатывает операцию уменьшения баланса на счету. Выводит сообщение если недостаточно средств для списания.
     * @param transaction транзакция для обработки.
     */
    private static void debit(Transaction transaction) {
        MoneyAccount moneyAccount = moneyAccountService.get(transaction.getMoneyAccountId());
        BigDecimal balance = moneyAccount.getBalance();
        if (balance.compareTo(transaction.getAmount()) < 0) {
            throw new TransactionException("Баланс меньше списываемой суммы");
        }
        else {
            moneyAccount.setBalance(moneyAccount.getBalance().subtract(transaction.getAmount()));
            moneyAccountService.updateBalance(moneyAccount);
        }
    }
}
