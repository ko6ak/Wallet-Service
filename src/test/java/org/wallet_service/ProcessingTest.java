package org.wallet_service;

import org.junit.jupiter.api.Test;
import org.wallet_service.in.PlayerController;
import org.wallet_service.in.TransactionController;
import org.wallet_service.util.Beans;
import org.wallet_service.util.Processing;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.CurrentPlayer.getToken;
import static org.wallet_service.util.CurrentPlayer.setToken;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class ProcessingTest extends AbstractServiceTest {
    private static final TransactionController transactionController = new TransactionController();
    private static final PlayerController playerController = Beans.getPlayerController();

    @Test
    void process() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        setToken(AbstractServiceTest.TOKEN);
        CONNECTION.createStatement().executeUpdate("TRUNCATE TABLE wallet.transaction; UPDATE wallet.money_account SET balance = 0 WHERE id = 1001");

        TransactionTO transactionTO = TransactionTestData.TRANSACTION_TO_1;

        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
                transactionTO.getDescription(), transactionTO.getToken());
        Processing.process();
        assertThat(playerController.getBalance(getToken())).isEqualTo("1000.00");

        transactionTO = TransactionTestData.TRANSACTION_TO_2;

        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
                transactionTO.getDescription(), transactionTO.getToken());
        Processing.process();
        assertThat(playerController.getBalance(getToken())).isEqualTo("300.01");

        transactionTO = TransactionTestData.TRANSACTION_TO_3;

        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
                transactionTO.getDescription(), transactionTO.getToken());
        Processing.process();
        assertThat(playerController.getBalance(getToken())).isEqualTo("0.00");

        transactionTO = TransactionTestData.TRANSACTION_TO_4;

        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
                transactionTO.getDescription(), transactionTO.getToken());
        Processing.process();
        assertThat(playerController.getBalance(getToken())).isEqualTo("0.00");

        transactionTO = TransactionTestData.TRANSACTION_TO_5;

        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
                transactionTO.getDescription(), transactionTO.getToken());
        Processing.process();
        assertThat(playerController.getBalance(getToken())).isEqualTo("444.44");
    }
}
