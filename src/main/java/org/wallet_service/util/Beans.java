package org.wallet_service.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.wallet_service.controller.TransactionController;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.service.PlayerService;
import org.wallet_service.service.TransactionService;
import org.wallet_service.controller.PlayerController;

/**
 * Класс позволяет получить различные, уже созданные, объекты.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Beans {
    private static final PlayerService playerService = new PlayerService();
    private static final MoneyAccountService moneyAccountService = new MoneyAccountService();
    private static final PlayerActionService playerActionService = new PlayerActionService();
    private static final TransactionService transactionService = new TransactionService();
    private static final TransactionController transactionController = new TransactionController();
    private static final PlayerController playerController = new PlayerController();

    public static PlayerService getPlayerService() {
        return playerService;
    }

    public static MoneyAccountService getMoneyAccountService() {
        return moneyAccountService;
    }

    public static PlayerActionService getPlayerActionService() {
        return playerActionService;
    }

    public static TransactionService getTransactionService() {
        return transactionService;
    }

    public static TransactionController getTransactionController() {
        return transactionController;
    }

    public static PlayerController getPlayerController() {
        return playerController;
    }
}
