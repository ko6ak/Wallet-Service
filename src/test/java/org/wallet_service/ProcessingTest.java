package org.wallet_service;

import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.wallet_service.config.SpringConfig;
import org.wallet_service.dto.request.LoginRequestDTO;
import org.wallet_service.dto.request.TokenRequestDTO;
import org.wallet_service.dto.request.TransactionRequestDTO;
import org.wallet_service.in.PlayerController;
import org.wallet_service.in.TransactionController;
import org.wallet_service.util.Processing;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.CurrentPlayer.getToken;
import static org.wallet_service.util.CurrentPlayer.setToken;

public class ProcessingTest extends AbstractServiceTest {
    @Autowired
    TransactionController transactionController;
    @Autowired
    PlayerController playerController;


//    @Autowired
//    public ProcessingTest(TransactionController transactionController, PlayerController playerController, Connection connection) {
//        super(connection);
//        this.transactionController = transactionController;
//        this.playerController = playerController;
//    }



    @Test
    void process() throws SQLException {
        playerController.login(new LoginRequestDTO(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword()));
        setToken(AbstractServiceTest.TOKEN);
        connection.createStatement().executeUpdate("TRUNCATE TABLE wallet.transaction; UPDATE wallet.money_account SET balance = 0 WHERE id = 1001");

        TransactionRequestDTO transactionRequestDTO = TransactionTestData.TRANSACTION_TO_1;

        transactionController.register(transactionRequestDTO);
        transactionController.process();
        assertThat(playerController.getBalance(new TokenRequestDTO(getToken())).getBody().getMessage()).isEqualTo("1000.00");

//        transactionTO = TransactionTestData.TRANSACTION_TO_2;
//
//        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
//                transactionTO.getDescription(), transactionTO.getToken());
//        Processing.process();
//        assertThat(playerController.getBalance(getToken())).isEqualTo("300.01");
//
//        transactionTO = TransactionTestData.TRANSACTION_TO_3;
//
//        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
//                transactionTO.getDescription(), transactionTO.getToken());
//        Processing.process();
//        assertThat(playerController.getBalance(getToken())).isEqualTo("0.00");
//
//        transactionTO = TransactionTestData.TRANSACTION_TO_4;
//
//        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
//                transactionTO.getDescription(), transactionTO.getToken());
//        Processing.process();
//        assertThat(playerController.getBalance(getToken())).isEqualTo("0.00");
//
//        transactionTO = TransactionTestData.TRANSACTION_TO_5;
//
//        transactionController.register(transactionTO.getId(), transactionTO.getOperation(), transactionTO.getAmount(),
//                transactionTO.getDescription(), transactionTO.getToken());
//        Processing.process();
//        assertThat(playerController.getBalance(getToken())).isEqualTo("444.44");
    }
}
