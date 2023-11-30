package org.wallet_service.processing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.TransactionTestData;
import org.wallet_service.dto.request.LoginRequestDTO;
import org.wallet_service.dto.request.TokenRequestDTO;
import org.wallet_service.dto.request.TransactionRequestDTO;
import org.wallet_service.in.PlayerController;
import org.wallet_service.in.TransactionController;
import org.wallet_service.service.TransactionService;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.CurrentPlayer.getToken;
import static org.wallet_service.util.CurrentPlayer.setToken;

public class ProcessingTest extends AbstractServiceTest {
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private PlayerController playerController;
    @Autowired
    private TransactionService transactionService;

    @Test
    void process() throws SQLException {
        playerController.login(new LoginRequestDTO(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword()));
        setToken(AbstractServiceTest.TOKEN);
        connection.createStatement().executeUpdate("TRUNCATE TABLE wallet.transaction; UPDATE wallet.money_account SET balance = 0 WHERE id = 1001");

        TransactionRequestDTO transactionRequestDTO = TransactionTestData.TRANSACTION_DTO_1;

        transactionController.register(transactionRequestDTO);
        transactionController.process();
        assertThat(playerController.getBalance(new TokenRequestDTO(getToken())).getBody().getMessage()).isEqualTo("1000.00");

        transactionRequestDTO = TransactionTestData.TRANSACTION_DTO_2;

        transactionController.register(transactionRequestDTO);
        transactionController.process();
        assertThat(playerController.getBalance(new TokenRequestDTO(getToken())).getBody().getMessage()).isEqualTo("300.01");

        transactionRequestDTO = TransactionTestData.TRANSACTION_DTO_3;

        transactionController.register(transactionRequestDTO);
        transactionController.process();
        assertThat(playerController.getBalance(new TokenRequestDTO(getToken())).getBody().getMessage()).isEqualTo("0.00");

        transactionRequestDTO = TransactionTestData.TRANSACTION_DTO_4;

        transactionController.register(transactionRequestDTO);
        transactionController.process();
        assertThat(playerController.getBalance(new TokenRequestDTO(getToken())).getBody().getMessage()).isEqualTo("0.00");

        transactionRequestDTO = TransactionTestData.TRANSACTION_DTO_5;

        transactionController.register(transactionRequestDTO);
        transactionController.process();
        assertThat(playerController.getBalance(new TokenRequestDTO(getToken())).getBody().getMessage()).isEqualTo("444.44");

        playerController.logout(new TokenRequestDTO(getToken()));

        connection.createStatement().executeUpdate("TRUNCATE TABLE wallet.transaction; UPDATE wallet.money_account SET balance = 0 WHERE id = 1001");
        transactionService.save(TransactionTestData.TRANSACTION_1);
        transactionService.save(TransactionTestData.TRANSACTION_2);
    }
}
