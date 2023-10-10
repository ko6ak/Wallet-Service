package org.wallet_service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wallet_service.controller.TransactionController;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.Beans;
import org.wallet_service.util.Processing;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessingTest {
    private static final TransactionController transactionController = new TransactionController();
    private static final MoneyAccountService moneyAccountService = Beans.getMoneyAccountService();
    private static final TransactionService transactionService = Beans.getTransactionService();

    @BeforeEach
    void clear() {
        transactionService.clear();
        moneyAccountService.clear();
    }

    @AfterAll
    static void clearAll() {
        transactionService.clear();
        moneyAccountService.clear();
    }

    @Test
    void process(){
        moneyAccountService.save(PlayerTestData.PLAYER_1.getMoneyAccount());
        transactionController.register(TransactionTestData.TRANSACTION_TO_1, PlayerTestData.PLAYER_1);
        Processing.process();
        assertThat(PlayerTestData.PLAYER_1.getMoneyAccount().getBalance()).isEqualTo(new BigDecimal("1000.00"));
        transactionController.register(TransactionTestData.TRANSACTION_TO_2, PlayerTestData.PLAYER_1);
        Processing.process();
        assertThat(PlayerTestData.PLAYER_1.getMoneyAccount().getBalance()).isEqualTo(new BigDecimal("300.01"));
        transactionController.register(TransactionTestData.TRANSACTION_TO_3, PlayerTestData.PLAYER_1);
        Processing.process();
        assertThat(PlayerTestData.PLAYER_1.getMoneyAccount().getBalance()).isEqualTo(new BigDecimal("0.00"));
        transactionController.register(TransactionTestData.TRANSACTION_TO_4, PlayerTestData.PLAYER_1);
        Processing.process();
        assertThat(PlayerTestData.PLAYER_1.getMoneyAccount().getBalance()).isEqualTo(new BigDecimal("0.00"));
        transactionController.register(TransactionTestData.TRANSACTION_TO_5, PlayerTestData.PLAYER_1);
        Processing.process();
        assertThat(PlayerTestData.PLAYER_1.getMoneyAccount().getBalance()).isEqualTo(new BigDecimal("444.44"));
    }
}
