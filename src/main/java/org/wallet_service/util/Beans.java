package org.wallet_service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.wallet_service.controller.TransactionController;
import org.wallet_service.service.*;
import org.wallet_service.controller.PlayerController;

/**
 * Класс позволяет получить различные, уже созданные, объекты.
 */
public final class Beans {
    private static final PlayerService playerService = new PlayerService();
    private static final MoneyAccountService moneyAccountService = new MoneyAccountService();
    private static final PlayerActionService playerActionService = new PlayerActionService();
    private static final MoneyAccountActionService moneyAccountActionService = new MoneyAccountActionService();
    private static final TransactionService transactionService = new TransactionService();
    private static final TransactionController transactionController = new TransactionController();
    private static final PlayerController playerController = new PlayerController();
    private static final ObjectMapper mapper = new ObjectMapper();

    private Beans() {
    }

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

    public static MoneyAccountActionService getMoneyAccountActionService() {
        return moneyAccountActionService;
    }

    public static ObjectMapper getObjectMapper() { return mapper; }
}
