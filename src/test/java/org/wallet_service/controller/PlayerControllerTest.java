package org.wallet_service.controller;

import org.junit.jupiter.api.Test;
import org.wallet_service.PlayerTestData;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.exception.MessageException;
import org.wallet_service.service.PlayerService;
import org.wallet_service.util.Beans;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class PlayerControllerTest extends AbstractServiceTest {
    private static final PlayerController playerController = Beans.getPlayerController();
    private static final PlayerService playerService = Beans.getPlayerService();

    @Test
    void registration(){
        assertThat(playerController.registration(PlayerTestData.PLAYER_TO)).isEqualTo(PlayerController.REGISTRATION_SUCCESS);
        assertThat(playerService.get(PlayerTestData.PLAYER_2.getId())).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_2);
        assertThatThrownBy(() -> playerController.registration(PlayerTestData.PLAYER_TO))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Игрок с таким логином уже есть в системе");
    }

    @Test
    void login() throws SQLException {
        assertThat(playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword())).usingRecursiveComparison()
                .isEqualTo(PlayerTestData.PLAYER_1_WITH_ID);
        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Сначала нужно выйти");
        playerController.logout();
        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_TO_WITH_BAD_LOGIN.getEmail(), PlayerTestData.PLAYER_TO_WITH_BAD_LOGIN.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Игрок с таким логином не найден");
        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_TO_WITH_BAD_PASSWORD.getEmail(), PlayerTestData.PLAYER_TO_WITH_BAD_PASSWORD.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Неправильный пароль");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id > 5");
    }

    @Test
    void logout() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        playerController.logout();
        assertThatThrownBy(playerController::logout)
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы не залогинены");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id > 5");
    }

    @Test
    void getBalance() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        assertThat(playerController.getBalance()).isEqualTo("Баланс вашего счета: 300.01");
        playerController.logout();
        assertThatThrownBy(playerController::getBalance)
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы не залогинены");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id > 5");
    }

    @Test
    void getTransactionLog() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        assertThat(playerController.getTransactionLog()).hasSize(2).hasSameElementsAs(PlayerTestData.MONEY_ACCOUNT_ACTIONS);
        playerController.logout();
        assertThatThrownBy(playerController::getTransactionLog)
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Сначала залогинтесь");
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id > 5");
    }

    @Test
    void getFullLog(){
        assertThat(playerController.getFullLog(1)).hasSize(7).usingRecursiveComparison().isEqualTo(PlayerTestData.FULL_PLAYER_ACTIONS);
        assertThatThrownBy(() -> playerController.getFullLog(8))
                .isInstanceOf(MessageException.class)
                .hasMessage("Игрок с таким id не найден");
    }
}
