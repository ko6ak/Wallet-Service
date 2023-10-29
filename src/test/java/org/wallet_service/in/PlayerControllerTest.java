package org.wallet_service.in;

import org.junit.jupiter.api.Test;
import org.wallet_service.PlayerTestData;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.exception.MessageException;
import org.wallet_service.util.Beans;
import org.wallet_service.util.CurrentPlayer;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class PlayerControllerTest extends AbstractServiceTest {
    private static final PlayerController playerController = Beans.getPlayerController();

    @Test
    void registration(){
        assertThat(playerController.registration(PlayerTestData.PLAYER_TO.getName(),
                PlayerTestData.PLAYER_TO.getEmail(),
                PlayerTestData.PLAYER_TO.getPassword())).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_2);

        assertThatThrownBy(() -> playerController.registration(PlayerTestData.PLAYER_TO.getName(),
                PlayerTestData.PLAYER_TO.getEmail(),
                PlayerTestData.PLAYER_TO.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Игрок с таким email уже есть в системе");
    }

    @Test
    void login() throws SQLException {
        assertThat(playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword()))
                .usingRecursiveComparison()
                .isEqualTo(PlayerTestData.PLAYER_1_WITH_ID);

        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Сначала нужно выйти");

        String token = CurrentPlayer.getToken();
        assertThat(token).isNotNull();
        playerController.logout(token);

        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_TO_WITH_BAD_LOGIN.getEmail(), PlayerTestData.PLAYER_TO_WITH_BAD_LOGIN.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Игрок с таким email не найден");

        assertThatThrownBy(() -> playerController.login(PlayerTestData.PLAYER_TO_WITH_BAD_PASSWORD.getEmail(), PlayerTestData.PLAYER_TO_WITH_BAD_PASSWORD.getPassword()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Неправильный пароль");

        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id > 5");
    }

    @Test
    void logout() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        playerController.logout(CurrentPlayer.getToken());
        assertThat(CurrentPlayer.getToken()).isNull();
        assertThat(CurrentPlayer.getCurrentPlayer()).isNull();

        assertThatThrownBy(() -> playerController.logout(AbstractServiceTest.TOKEN))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы не залогинены");

        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());

        CurrentPlayer.setToken(AbstractServiceTest.TOKEN);
        assertThatThrownBy(() -> playerController.logout(AbstractServiceTest.OTHER_TOKEN))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы не залогинены");

        playerController.logout(CurrentPlayer.getToken());

        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id > 5");
    }

    @Test
    void getBalance() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());

        String token = CurrentPlayer.getToken();
        assertThat(playerController.getBalance(token)).isEqualTo("300.01");
        playerController.logout(token);

        assertThatThrownBy(() -> playerController.getBalance(token))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы не залогинены");

        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());

        CurrentPlayer.setToken(AbstractServiceTest.TOKEN);
        assertThatThrownBy(() -> playerController.getBalance(AbstractServiceTest.OTHER_TOKEN))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Вы не залогинены");

        CurrentPlayer.setToken(AbstractServiceTest.EXPIRED_TOKEN);
        assertThatThrownBy(() -> playerController.getBalance(AbstractServiceTest.EXPIRED_TOKEN))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Токен просрочен, залогинтесь заново");
        assertThat(CurrentPlayer.getToken()).isNull();
        assertThat(CurrentPlayer.getCurrentPlayer()).isNull();

        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player_actions WHERE id > 5");
    }

    @Test
    void getTransactionLog() throws SQLException {
        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        String token = CurrentPlayer.getToken();
        assertThat(playerController.getTransactionLog(token)).hasSize(2).hasSameElementsAs(PlayerTestData.MONEY_ACCOUNT_ACTIONS);
        playerController.logout(token);

        assertThatThrownBy(() -> playerController.getTransactionLog(token))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Сначала залогинтесь");

        playerController.login(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());

        CurrentPlayer.setToken(AbstractServiceTest.TOKEN);
        assertThatThrownBy(() -> playerController.getTransactionLog(AbstractServiceTest.OTHER_TOKEN))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Сначала залогинтесь");

        CurrentPlayer.setToken(AbstractServiceTest.EXPIRED_TOKEN);
        assertThatThrownBy(() -> playerController.getTransactionLog(AbstractServiceTest.EXPIRED_TOKEN))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Токен просрочен, залогинтесь заново");
        assertThat(CurrentPlayer.getToken()).isNull();
        assertThat(CurrentPlayer.getCurrentPlayer()).isNull();

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
