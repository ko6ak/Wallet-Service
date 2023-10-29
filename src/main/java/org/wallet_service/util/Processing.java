package org.wallet_service.util;

import org.springframework.stereotype.Component;
import org.wallet_service.entity.*;
import org.wallet_service.service.MoneyAccountService;

import java.math.BigDecimal;

/**
 * Класс, отвечающий за обработку зарегистрированных транзакций.
 */
@Component
public class Processing {
    private final MoneyAccountService moneyAccountService;

    public Processing(MoneyAccountService moneyAccountService) {
        this.moneyAccountService = moneyAccountService;
    }

    /**
     * Метод обрабатывает операцию увеличения баланса на счету.
     * @param transaction транзакция для обработки.
     */
    public boolean credit(Transaction transaction) {
        MoneyAccount moneyAccount = moneyAccountService.get(transaction.getMoneyAccountId());
        moneyAccount.setBalance(moneyAccount.getBalance().add(transaction.getAmount()));
        return moneyAccountService.updateBalance(moneyAccount);
    }

    /**
     * Метод обрабатывает операцию уменьшения баланса на счету. Выводит сообщение если недостаточно средств для списания.
     * @param transaction транзакция для обработки.
     */
    public boolean debit(Transaction transaction) {
        MoneyAccount moneyAccount = moneyAccountService.get(transaction.getMoneyAccountId());
        BigDecimal balance = moneyAccount.getBalance();
        if (balance.compareTo(transaction.getAmount()) < 0) {
            return false;
        }
        else {
            moneyAccount.setBalance(moneyAccount.getBalance().subtract(transaction.getAmount()));
            return moneyAccountService.updateBalance(moneyAccount);
        }
    }
}
