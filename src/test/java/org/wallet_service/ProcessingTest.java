package org.wallet_service;

import org.junit.jupiter.api.Test;
import org.wallet_service.controller.PlayerController;
import org.wallet_service.controller.TransactionController;
import org.wallet_service.util.Beans;
import org.wallet_service.util.Processing;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class ProcessingTest extends AbstractServiceTest {
    private static final TransactionController transactionController = new TransactionController();
    private static final PlayerController playerController = Beans.getPlayerController();

    @Test
    void process() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getLogin(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        CONNECTION.createStatement().executeUpdate("TRUNCATE TABLE wallet.transaction; UPDATE wallet.money_account SET balance = 0 WHERE id = 1001");
        transactionController.register(TransactionTestData.TRANSACTION_TO_1, PlayerTestData.PLAYER_1_WITH_ID);
        Processing.process();
        assertThat(playerController.getBalance()).isEqualTo("Баланс вашего счета: 1000.00");
        transactionController.register(TransactionTestData.TRANSACTION_TO_2, PlayerTestData.PLAYER_1_WITH_ID);
        Processing.process();
        assertThat(playerController.getBalance()).isEqualTo("Баланс вашего счета: 300.01");
        transactionController.register(TransactionTestData.TRANSACTION_TO_3, PlayerTestData.PLAYER_1_WITH_ID);
        Processing.process();
        assertThat(playerController.getBalance()).isEqualTo("Баланс вашего счета: 0.00");
        transactionController.register(TransactionTestData.TRANSACTION_TO_4, PlayerTestData.PLAYER_1_WITH_ID);
        Processing.process();
        assertThat(playerController.getBalance()).isEqualTo("Баланс вашего счета: 0.00");
        transactionController.register(TransactionTestData.TRANSACTION_TO_5, PlayerTestData.PLAYER_1_WITH_ID);
        Processing.process();
        assertThat(playerController.getBalance()).isEqualTo("Баланс вашего счета: 444.44");
    }
}
