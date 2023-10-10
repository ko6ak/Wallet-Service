package org.wallet_service.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wallet_service.PlayerTestData;
import org.wallet_service.TransactionTestData;
import org.wallet_service.entity.Transaction;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.Beans;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class TransactionControllerTest {
    private static final TransactionController transactionController = Beans.getTransactionController();
    private static final TransactionService transactionService = Beans.getTransactionService();

    @BeforeEach
    void clear() {
        transactionService.clear();
    }

    @AfterAll
    static void clearAll() {
        transactionService.clear();
    }

    @Test
    void register(){
        List<Transaction> transactions = new ArrayList<>();
        TransactionTestData.NOT_PROCESSED_TRANSACTIONS_TOS.forEach(t -> assertThat(transactionController.register(t, PlayerTestData.PLAYER_1))
                .isEqualTo(TransactionController.REGISTER_SUCCESS));
        TransactionTestData.NOT_PROCESSED_TRANSACTIONS_IDS.forEach(id -> transactions.add(transactionService.get(id)));
        assertThat(transactions).hasSize(3).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime")
                .containsAll(TransactionTestData.NOT_PROCESSED_TRANSACTIONS);
        assertThatThrownBy(() -> transactionController.register(TransactionTestData.TRANSACTION_WITHOUT_ID, PlayerTestData.PLAYER_1))
                .isInstanceOf(TransactionException.class)
                .hasMessage("Нет id транзакции");
        assertThatThrownBy(() -> transactionController.register(TransactionTestData.TRANSACTION_TO_3, PlayerTestData.PLAYER_1))
                .isInstanceOf(TransactionException.class)
                .hasMessage("Не уникальный id транзакции");
    }

    @Test
    void getNotProcessed(){
        List<Transaction> transactions = new ArrayList<>();
        TransactionTestData.TRANSACTIONS_TOS.forEach(t -> assertThat(transactionController.register(t, PlayerTestData.PLAYER_1))
                .isEqualTo(TransactionController.REGISTER_SUCCESS));
        TransactionTestData.TRANSACTIONS_IDS.forEach(id -> transactions.add(transactionService.get(id)));
        assertThat(transactions).hasSize(5).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime")
                .containsAll(TransactionTestData.NOT_PROCESSED_TRANSACTIONS);
    }
}
