package org.wallet_service.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.MoneyAccountActionService;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.entity.Action;
import org.wallet_service.controller.TransactionController;
import org.wallet_service.entity.MoneyAccount;
import org.wallet_service.entity.Transaction;
import org.wallet_service.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс, отвечающий за обработку зарегистрированных транзакций.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Processing {
    private static final TransactionController transactionController = Beans.getTransactionController();
    private static final TransactionService transactionService = Beans.getTransactionService();
    private static final MoneyAccountService moneyAccountService = Beans.getMoneyAccountService();
    private static final MoneyAccountActionService moneyAccountActionService = Beans.getMoneyAccountActionService();

    /**
     * Метод получает не обработанные транзакции и обрабатывает их в соответствии с типом операции.
     * Выводит сообщение о завершении обработки или если транзакций для обработки нет. Не прерывает работу если транзакцию выполнить не удалось.
     */
    public static void process(){
        List<Transaction> transactions = transactionController.getNotProcessed();
        if (!transactions.isEmpty()) {
            transactions.forEach(t -> {
                switch (t.getOperation()) {
                    case CREDIT -> credit(t);
                    case DEBIT -> debit(t);
                }
                t.setProcessed(true);
                transactionService.save(t);
                //надо сохранять в базу транзакцию с true

            });
            System.out.println("Все транзакции обработаны");
        }
        else System.out.println("Нет транзакций для обработки");
    }

    /**
     * Метод обрабатывает операцию увеличения баланса на счету.
     * @param transaction транзакция для обработки.
     */
    private static void credit(Transaction transaction) {
        MoneyAccount moneyAccount = moneyAccountService.get(transaction.getMoneyAccountId());
        moneyAccount.setBalance(moneyAccount.getBalance().add(transaction.getAmount()));
        System.out.println("Транзакция с id '" + transaction.getId() + "' успешно обработана");
        logging(transaction, moneyAccount.getLog(), moneyAccount.getPlayerId());
    }

    /**
     * Метод обрабатывает операцию уменьшения баланса на счету. Выводит сообщение если недостаточно средств для списания.
     * @param transaction транзакция для обработки.
     */
    private static void debit(Transaction transaction) {
        MoneyAccount moneyAccount = moneyAccountService.get(transaction.getMoneyAccountId());
        BigDecimal balance = moneyAccount.getBalance();
        if (balance.compareTo(transaction.getAmount()) < 0) {
            String message = "Баланс меньше списываемой суммы";
            logging(transaction, moneyAccount.getLog(), moneyAccount.getPlayerId(), message);
            System.out.println("Транзакция с id '" + transaction.getId() + "' завершена с ошибкой: " + message);
        }
        else {
            moneyAccount.setBalance(moneyAccount.getBalance().subtract(transaction.getAmount()));
            System.out.println("Транзакция с id '" + transaction.getId() + "' успешно обработана");
            logging(transaction, moneyAccount.getLog(), moneyAccount.getPlayerId());
        }
    }

    /**
     * Логирует успешную операцию списания или пополнения, сохраняет транзакцию в списке выполненных транзакций счета.
     * @param transaction транзакция для обработки.
     * @param log лог изменений счета.
     * @param playerId идентификатор Игрока.
     */
    private static void logging(Transaction transaction, List<Action> log, long playerId){
        BigDecimal amount = transaction.getAmount();
        Action action = new Action(playerId, transaction.getDateTime(),
                "Транзакция с типом операции " + transaction.getOperation() +
                        ", суммой " + (amount.toString().contains(".") ? amount : amount + ".00") +
                        " и комментарием '" + transaction.getDescription() + "' успешно выполнена");
        moneyAccountActionService.add(action);
        log.add(action);
    }

    /**
     * Логирует операцию списания или пополнения, которая не удалась, сохраняет транзакцию в списке выполненных транзакций счета.
     * @param transaction транзакция для обработки.
     * @param log лог изменений счета.
     * @param playerId идентификатор Игрока.
     * @param message причина отмены обработки транзакции.
     */
    private static void logging(Transaction transaction, List<Action> log, long playerId, String message){
        BigDecimal amount = transaction.getAmount();
        Action action = new Action(playerId, transaction.getDateTime(),
                "Транзакция с типом операции " + transaction.getOperation() +
                        ", суммой " + (amount.toString().contains(".") ? amount : amount + ".00") +
                        " и комментарием '" + transaction.getDescription() + "' не выполнена. Причина: " + message);
        moneyAccountActionService.add(action);
        log.add(action);
    }
}
