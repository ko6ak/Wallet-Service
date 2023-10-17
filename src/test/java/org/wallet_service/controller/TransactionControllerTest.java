package org.wallet_service.controller;

import org.junit.jupiter.api.Test;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.TransactionTestData;
import org.wallet_service.entity.Transaction;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.Beans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class TransactionControllerTest extends AbstractServiceTest {
    private static final TransactionController transactionController = Beans.getTransactionController();
    private static final TransactionService transactionService = Beans.getTransactionService();

    @Test
    void register() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        TransactionTestData.NOT_PROCESSED_TRANSACTIONS_TOS.forEach(t -> assertThat(transactionController.register(t, PlayerTestData.PLAYER_1_WITH_ID))
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
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'bae26c79-e40b-495d-87b0-24255a9d383a'");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'acb79fd6-88a4-455f-ac10-394ad7c0f336'");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = '711c12eb-9f98-417d-af8f-57f902d3000e'");
    }

    @Test
    void getNotProcessed() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        TransactionTestData.TRANSACTIONS_TOS.forEach(t -> assertThat(transactionController.register(t, PlayerTestData.PLAYER_1_WITH_ID))
                .isEqualTo(TransactionController.REGISTER_SUCCESS));
        TransactionTestData.TRANSACTIONS_IDS.forEach(id -> transactions.add(transactionService.get(id)));
        assertThat(transactions).hasSize(3).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime")
                .containsAll(TransactionTestData.NOT_PROCESSED_TRANSACTIONS);
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'bae26c79-e40b-495d-87b0-24255a9d383a'");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'acb79fd6-88a4-455f-ac10-394ad7c0f336'");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = '711c12eb-9f98-417d-af8f-57f902d3000e'");
    }
}
