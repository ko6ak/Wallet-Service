package org.wallet_service.in;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.dto.response.PlayerResponseDTO;
import org.wallet_service.entity.MoneyAccount;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.service.MoneyAccountActionService;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.service.PlayerService;
import org.wallet_service.util.CurrentPlayer;
import org.wallet_service.util.JWT;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PlayerControllerTest extends AbstractControllerTest{

    @Mock
    private PlayerService playerService;
    @Mock
    private MoneyAccountService moneyAccountService;
    @Mock
    private PlayerActionService playerActionService;
    @Mock
    private MoneyAccountActionService moneyAccountActionService;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new PlayerController(
                playerService,
                moneyAccountService,
                playerActionService,
                moneyAccountActionService,
                validator
        )).build();

        validationMessages.put("email", Collections.singletonList("неправильный email"));
    }

    @Test
    void registrationOK() throws Exception {
        Player player = PlayerTestData.PLAYER_2;
        MoneyAccount moneyAccount = PlayerTestData.NEW_ACCOUNT_FOR_PLAYER_2;

        when(moneyAccountService.save(new MoneyAccount(new BigDecimal("0.00")))).thenReturn(PlayerTestData.NEW_ACCOUNT_FOR_PLAYER_2);
        when(playerService.save(PlayerTestData.NEW_PLAYER_2)).thenReturn(PlayerTestData.PLAYER_2);

        AtomicReference<PlayerResponseDTO> playerResponseDTO = new AtomicReference<>();

        mvc.perform(post("/registration")
                        .content(mapper.writeValueAsString(PlayerTestData.PLAYER_2_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.id").value(player.getId()))
                .andExpect(jsonPath("$.name").value(player.getName()))
                .andExpect(jsonPath("$.email").value(player.getEmail()))
                .andExpect(jsonPath("$.token").isEmpty())
                .andExpect(jsonPath("$.moneyAccount.id").value(moneyAccount.getId()))
//                .andExpect(jsonPath("$.moneyAccount.balance").value(moneyAccount.getBalance()))
//                возвращает 0.0, то есть автоматически конвертирует в double, а нужно 0.00
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    playerResponseDTO.set(mapper.readValue(response, PlayerResponseDTO.class));
                });

        assertThat(playerResponseDTO.get().getMoneyAccount().getBalance()).isEqualTo(moneyAccount.getBalance());
    }

    @Test
    void registrationUnauthorized() throws Exception {
        when(playerService.isFound(PlayerTestData.PLAYER_1_WITH_ID.getEmail())).thenReturn(true);

        mvc.perform(post("/registration")
                        .content(mapper.writeValueAsString(PlayerTestData.PLAYER_1_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.EMAIL_IS_ALREADY_EXIST));
    }

    @Test
    void registrationBadRequest() throws Exception {
        when(validator.getResult()).thenReturn(validationMessages);

        mvc.perform(post("/registration")
                        .content(mapper.writeValueAsString(PlayerTestData.PLAYER_2_WITH_WRONG_EMAIL_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.email").value(validationMessages.get("email").get(0)));
    }

    @Test
    void loginOK() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(null);
        JWT_MOCKED_STATIC.when(() -> JWT.create(PlayerTestData.PLAYER_1_WITH_ID)).thenReturn(AbstractServiceTest.TOKEN);
        when(playerService.isFound(PlayerTestData.PLAYER_1_WITH_ID.getEmail())).thenReturn(true);
        when(playerService.get(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword())).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);

        Player player = PlayerTestData.PLAYER_1_WITH_ID;
        MoneyAccount moneyAccount = PlayerTestData.ACCOUNT_1001;

        AtomicReference<PlayerResponseDTO> playerResponseDTO = new AtomicReference<>();

        mvc.perform(post("/login")
                        .content(mapper.writeValueAsString(PlayerTestData.PLAYER_1_LOGIN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.id").value(player.getId()))
                .andExpect(jsonPath("$.name").value(player.getName()))
                .andExpect(jsonPath("$.email").value(player.getEmail()))
                .andExpect(jsonPath("$.token").value(AbstractServiceTest.TOKEN))
                .andExpect(jsonPath("$.moneyAccount.id").value(moneyAccount.getId()))
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    playerResponseDTO.set(mapper.readValue(response, PlayerResponseDTO.class));
                });

        assertThat(playerResponseDTO.get().getMoneyAccount().getBalance()).isEqualTo(moneyAccount.getBalance());
    }

    @Test
    void loginTwice() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);

        mvc.perform(post("/login")
                        .content(mapper.writeValueAsString(PlayerTestData.PLAYER_1_LOGIN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.EXIT_FIRST));
    }

    @Test
    void loginBadEmail() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(null);
        when(playerService.isFound(PlayerTestData.PLAYER_1_WITH_ID.getEmail())).thenReturn(false);

        mvc.perform(post("/login")
                        .content(mapper.writeValueAsString(PlayerTestData.PLAYER_1_LOGIN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.PLAYER_WITH_THIS_EMAIL_NOT_FOUND));
    }

    @Test
    void loginBadPassword() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(null);
        when(playerService.isFound(PlayerTestData.PLAYER_1_WITH_ID.getEmail())).thenReturn(true);
        when(playerService.get(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword())).thenReturn(null);

        mvc.perform(post("/login")
                        .content(mapper.writeValueAsString(PlayerTestData.PLAYER_1_LOGIN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.WRONG_PASSWORD));
    }

    @Test
    void loginBadRequest() throws Exception {
        when(validator.getResult()).thenReturn(validationMessages);

        mvc.perform(post("/login")
                        .content(mapper.writeValueAsString(PlayerTestData.PLAYER_2_WITH_WRONG_EMAIL_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.email").value(validationMessages.get("email").get(0)));
    }

    @Test
    void logoutOK() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/logout")
                        .content(mapper.writeValueAsString(AbstractServiceTest.TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.GOODBYE));
    }

    @Test
    void logoutNullPlayer() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(null);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/logout")
                        .content(mapper.writeValueAsString(AbstractServiceTest.TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.NOT_LOGGED_IN));
    }

    @Test
    void logoutBadToken() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/logout")
                        .content(mapper.writeValueAsString(AbstractServiceTest.OTHER_TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.NOT_LOGGED_IN));
    }

    @Test
    void getBalanceOK() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);
        when(playerService.get(PlayerTestData.PLAYER_1_WITH_ID.getId())).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);

        mvc.perform(post("/balance")
                        .content(mapper.writeValueAsString(AbstractServiceTest.TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerTestData.ACCOUNT_1001.getBalance().toString()));
    }

    @Test
    void getBalanceExpiredToken() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.EXPIRED_TOKEN);
        JWT_MOCKED_STATIC.when(() -> JWT.validate(AbstractServiceTest.EXPIRED_TOKEN)).thenThrow(AuthenticationException.class);

        mvc.perform(post("/balance")
                        .content(mapper.writeValueAsString(AbstractServiceTest.EXPIRED_TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.EXPIRED_TOKEN));
    }

    @Test
    void getBalanceNullPlayer() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(null);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/balance")
                        .content(mapper.writeValueAsString(AbstractServiceTest.TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.NOT_LOGGED_IN));
    }

    @Test
    void getBalanceBadToken() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/balance")
                        .content(mapper.writeValueAsString(AbstractServiceTest.OTHER_TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.NOT_LOGGED_IN));
    }

    @Test
    void getTransactionLogOK() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);
        when(moneyAccountActionService.get(PlayerTestData.PLAYER_1_WITH_ID.getMoneyAccount().getId())).thenReturn(new ArrayList<>(PlayerTestData.MONEY_ACCOUNT_ACTIONS));

        AtomicReference<String> response = new AtomicReference<>();

        mvc.perform(post("/transaction-log")
                        .content(mapper.writeValueAsString(AbstractServiceTest.TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andDo(result -> response.set(result.getResponse().getContentAsString(StandardCharsets.UTF_8)));

        assertThat(response.get()).isEqualTo(mapper.writeValueAsString(PlayerTestData.MONEY_ACCOUNT_ACTIONS_RESPONSE));
    }

    @Test
    void getTransactionLogExpiredToken() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.EXPIRED_TOKEN);
        JWT_MOCKED_STATIC.when(() -> JWT.validate(AbstractServiceTest.EXPIRED_TOKEN)).thenThrow(AuthenticationException.class);

        mvc.perform(post("/transaction-log")
                        .content(mapper.writeValueAsString(AbstractServiceTest.EXPIRED_TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.EXPIRED_TOKEN));
    }

    @Test
    void getTransactionLogNullPlayer() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(null);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/transaction-log")
                        .content(mapper.writeValueAsString(AbstractServiceTest.TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.NOT_LOGGED_IN));
    }

    @Test
    void getTransactionLogBadToken() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/transaction-log")
                        .content(mapper.writeValueAsString(AbstractServiceTest.OTHER_TOKEN_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.NOT_LOGGED_IN));
    }

    @Test
    void getFullLogOK() throws Exception {
        when(playerService.get(PlayerTestData.PLAYER_1_WITH_ID.getId())).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        when(moneyAccountActionService.get(PlayerTestData.PLAYER_1_WITH_ID.getMoneyAccount().getId())).thenReturn(new ArrayList<>(PlayerTestData.MONEY_ACCOUNT_ACTIONS));
        when(playerActionService.get(PlayerTestData.PLAYER_1_WITH_ID.getId())).thenReturn(new ArrayList<>(PlayerTestData.ACTIONS_1));

        AtomicReference<String> response = new AtomicReference<>();

        mvc.perform(get("/full-log").param("id", String.valueOf(PlayerTestData.PLAYER_1_WITH_ID.getId())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andDo(result -> response.set(result.getResponse().getContentAsString(StandardCharsets.UTF_8)));

        assertThat(response.get()).isEqualTo(mapper.writeValueAsString(PlayerTestData.FULL_PLAYER_ACTIONS_RESPONSE));
    }

    @Test
    void getFullLogBadId() throws Exception {
        when(playerService.get(PlayerTestData.PLAYER_2_WITH_ID.getId())).thenReturn(null);

        mvc.perform(get("/full-log").param("id", String.valueOf(PlayerTestData.PLAYER_2_WITH_ID.getId())))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(PlayerController.PLAYER_WITH_THIS_ID_NOT_FOUND));
    }
}