package org.wallet_service.controller;

import org.junit.jupiter.api.Test;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.TransactionTO;
import org.wallet_service.TransactionTestData;
import org.wallet_service.entity.Transaction;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.Beans;
import org.wallet_service.util.CurrentPlayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class TransactionControllerTest extends AbstractServiceTest {
    private static final TransactionController transactionController = Beans.getTransactionController();
    private static final TransactionService transactionService = Beans.getTransactionService();
    private static final PlayerController playerController = Beans.getPlayerController();

    @Test
    void register() throws SQLException {
        TransactionTO transactionTO = TransactionTestData.TRANSACTION_TO_3;

        assertThatThrownBy(() -> transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
                transactionTO.getDescription(), transactionTO.getToken()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Сначала нужно залогинится");

        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());

        assertThatThrownBy(() -> transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
                transactionTO.getDescription(), transactionTO.getToken()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Сначала нужно залогинится");

        CurrentPlayer.setToken(AbstractServiceTest.EXPIRED_TOKEN);
        TransactionTO transactionTO_1 = TransactionTestData.TRANSACTION_TO_WITH_EXPIRED_TOKEN;
        assertThatThrownBy(() -> transactionController.register(transactionTO_1.getId(), transactionTO_1.getOperation(), transactionTO_1.getAmount(),
                transactionTO_1.getDescription(), transactionTO_1.getToken()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Токен просрочен, залогинтесь заново");
        assertThat(CurrentPlayer.getToken()).isNull();
        assertThat(CurrentPlayer.getCurrentPlayer()).isNull();

        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());

        CurrentPlayer.setToken(AbstractServiceTest.TOKEN);
        TransactionTestData.NOT_PROCESSED_TRANSACTIONS_TOS.forEach(t -> assertThat(transactionController.register(
                t.getId(), t.getOperation(), t.getAmount(), t.getDescription(), t.getToken()))
                .isEqualTo(TransactionController.REGISTER_SUCCESS));

        List<Transaction> transactions = new ArrayList<>();
        TransactionTestData.NOT_PROCESSED_TRANSACTIONS_IDS.forEach(id -> transactions.add(transactionService.get(id)));
        assertThat(transactions).hasSize(3).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime")
                .containsAll(TransactionTestData.NOT_PROCESSED_TRANSACTIONS);

        assertThatThrownBy(() -> transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
                transactionTO.getDescription(), transactionTO.getToken()))
                .isInstanceOf(TransactionException.class)
                .hasMessage("Не уникальный id транзакции");

        playerController.logout(CurrentPlayer.getToken());

        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'bae26c79-e40b-495d-87b0-24255a9d383a'");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'acb79fd6-88a4-455f-ac10-394ad7c0f336'");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = '711c12eb-9f98-417d-af8f-57f902d3000e'");
    }

    @Test
    void getNotProcessed() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        CurrentPlayer.setToken(AbstractServiceTest.TOKEN);

        TransactionTestData.TRANSACTIONS_TOS.forEach(t -> assertThat(transactionController.register(
                t.getId(), t.getOperation(), t.getAmount(), t.getDescription(), t.getToken()))
                .isEqualTo(TransactionController.REGISTER_SUCCESS));

        List<Transaction> transactions = new ArrayList<>();
        TransactionTestData.TRANSACTIONS_IDS.forEach(id -> transactions.add(transactionService.get(id)));
        assertThat(transactions).hasSize(3).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime")
                .containsAll(TransactionTestData.NOT_PROCESSED_TRANSACTIONS);

        playerController.logout(CurrentPlayer.getToken());

        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'bae26c79-e40b-495d-87b0-24255a9d383a'");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = 'acb79fd6-88a4-455f-ac10-394ad7c0f336'");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.transaction WHERE id = '711c12eb-9f98-417d-af8f-57f902d3000e'");
    }
}
