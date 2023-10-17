package org.wallet_service.service;

import org.junit.jupiter.api.Test;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.TransactionTestData;
import org.wallet_service.util.Beans;

import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class TransactionServiceTest extends AbstractServiceTest {
    private static final TransactionService transactionService = Beans.getTransactionService();

    @Test
    void save() throws SQLException {
        transactionService.save(TransactionTestData.TRANSACTION_3);
        assertThat(transactionService.get(TransactionTestData.TRANSACTION_3.getId())).usingRecursiveComparison().isEqualTo(TransactionTestData.TRANSACTION_3);
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'bae26c79-e40b-495d-87b0-24255a9d383a'");
    }

    @Test
    void get(){
        assertThat(transactionService.get(TransactionTestData.TRANSACTION_1.getId())).usingRecursiveComparison().isEqualTo(TransactionTestData.TRANSACTION_1);
    }

    @Test
    void isFound(){
        assertThat(transactionService.isFound(TransactionTestData.TRANSACTION_1.getId())).isTrue();
        assertThat(transactionService.isFound(UUID.fromString("4e42a3f4-dcf4-49a2-9963-c7f49ca1b146"))).isFalse();
    }

    @Test
    void getNotProcessed() throws SQLException {
        TransactionTestData.NOT_PROCESSED_TRANSACTIONS.forEach(transactionService::save);
        assertThat(TransactionTestData.NOT_PROCESSED_TRANSACTIONS).isEqualTo(transactionService.getNotProcessed());
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE is_processed = FALSE");
    }

    @Test
    void updateProcessed() throws SQLException {
        transactionService.save(TransactionTestData.TRANSACTION_3);
        transactionService.updateProcessed(TransactionTestData.TRANSACTION_3);
        assertThat(transactionService.get(TransactionTestData.TRANSACTION_3.getId()).isProcessed()).isTrue();
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'bae26c79-e40b-495d-87b0-24255a9d383a'");
    }
}
