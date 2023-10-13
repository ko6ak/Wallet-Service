package org.wallet_service.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wallet_service.PlayerTestData;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.MessageException;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.service.PlayerService;
import org.wallet_service.util.Beans;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PlayerControllerTest {
    private static final PlayerController playerController = Beans.getPlayerController();
    private static final PlayerService playerService = Beans.getPlayerService();
    private static final PlayerActionService playerActionService = Beans.getPlayerActionService();
    private static final MoneyAccountService moneyAccountService = Beans.getMoneyAccountService();
    private static String registration;

    @BeforeEach
    void clear() {
        playerService.clear();
        moneyAccountService.clear();
        registration = playerController.registration(PlayerTestData.PLAYER_TO);
    }

    @AfterAll
    static void clearAll() {
        playerService.clear();
        moneyAccountService.clear();
    }

    @Test
    void registration(){
        assertThat(registration).isEqualTo(PlayerController.REGISTRATION_SUCCESS);
        assertThat(playerController.get(PlayerTestData.PLAYER_1.getId())).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_1);
        assertThatThrownBy(() -> playerController.registration(PlayerTestData.PLAYER_TO))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Игрок с таким логином уже есть в системе");
    }

    @Test
    void login(){
        assertThat(playerController.login(PlayerTestData.PLAYER_TO.getLogin(), PlayerTestData.PLAYER_TO.getPassword())).usingRecursiveComparison()
                .isEqualTo(PlayerTestData.PLAYER_1);
        playerController.logout();
        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_TO_WITH_BAD_LOGIN.getLogin(), PlayerTestData.PLAYER_TO_WITH_BAD_LOGIN.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Игрок с таким логином не найден");
        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_TO_WITH_BAD_PASSWORD.getLogin(), PlayerTestData.PLAYER_TO_WITH_BAD_PASSWORD.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Неправильный пароль");
        assertThat(playerController.login(PlayerTestData.PLAYER_TO.getLogin(), PlayerTestData.PLAYER_TO.getPassword())).usingRecursiveComparison()
                .isEqualTo(PlayerTestData.PLAYER_1);
        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_TO.getLogin(), PlayerTestData.PLAYER_TO.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы уже залогинены");
        playerController.logout();
    }

    @Test
    void logout(){
        assertThat(playerController.login(PlayerTestData.PLAYER_TO.getLogin(), PlayerTestData.PLAYER_TO.getPassword())).usingRecursiveComparison()
                .isEqualTo(PlayerTestData.PLAYER_1);
        playerController.logout();
        assertThatThrownBy(playerController::logout)
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы не залогинены");
    }

    @Test
    void get(){
        assertThat(playerController.get(PlayerTestData.PLAYER_1.getId())).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_1);
        assertThat(playerController.get(5)).isNull();
    }

    @Test
    void getBalance(){
        playerController.login(PlayerTestData.PLAYER_TO.getLogin(), PlayerTestData.PLAYER_TO.getPassword());
        playerController.get(PlayerTestData.PLAYER_1.getId()).getMoneyAccount().setBalance(new BigDecimal("2506.31"));
        assertThat(playerController.getBalance()).isEqualTo("Баланс вашего счета: 2506.31");
        playerController.logout();
        assertThatThrownBy(playerController::getBalance)
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы не залогинены");
    }

    @Test
    void getTransactionLog(){
        playerController.login(PlayerTestData.PLAYER_TO.getLogin(), PlayerTestData.PLAYER_TO.getPassword());
//        playerController.get(PlayerTestData.PLAYER_1.getId()).getMoneyAccount().setLog(PlayerTestData.TRANSACTION_ACTIONS);
        assertThat(playerController.getTransactionLog()).hasSize(2).hasSameElementsAs(PlayerTestData.TRANSACTION_ACTIONS);
        playerController.logout();
        assertThatThrownBy(playerController::getTransactionLog)
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Сначала залогинтесь");
    }

    @Test
    void getFullLog(){
        playerActionService.clear();
        PlayerTestData.FULL_ACTIONS.forEach(a -> playerActionService.add(1, a));
        assertThat(playerController.getFullLog(1)).hasSize(7).usingRecursiveComparison().isEqualTo(PlayerTestData.FULL_ACTIONS);
        assertThatThrownBy(() -> playerController.getFullLog(8))
                .isInstanceOf(MessageException.class)
                .hasMessage("Игрок с таким id не найден");
    }
}
