package org.wallet_service.in;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.TransactionTestData;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.CurrentPlayer;
import org.wallet_service.util.JWT;
import org.wallet_service.util.Processing;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionControllerTest extends AbstractControllerTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private Processing processing;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new TransactionController(
                transactionService,
                validator,
                processing
        )).build();

        validationMessages.put("operation", Collections.singletonList("не должно быть пустым"));
    }

    @Test
    void registerOK() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);
        when(transactionService.isFound(TransactionTestData.ID3)).thenReturn(false);

        mvc.perform(post("/transaction-registration")
                        .content(mapper.writeValueAsString(TransactionTestData.TRANSACTION_DTO_3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(TransactionController.REGISTER_SUCCESS));
    }

    @Test
    void registerNotUniqueTransaction() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);
        when(transactionService.isFound(TransactionTestData.ID3)).thenReturn(true);

        mvc.perform(post("/transaction-registration")
                        .content(mapper.writeValueAsString(TransactionTestData.TRANSACTION_DTO_3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(TransactionController.NOT_UNIQUE_TRANSACTION_ID));
    }

    @Test
    void  registerExpiredToken() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.EXPIRED_TOKEN);
        JWT_MOCKED_STATIC.when(() -> JWT.validate(AbstractServiceTest.EXPIRED_TOKEN)).thenThrow(AuthenticationException.class);

        mvc.perform(post("/transaction-registration")
                        .content(mapper.writeValueAsString(TransactionTestData.TRANSACTION_TO_WITH_EXPIRED_TOKEN))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(TransactionController.EXPIRED_TOKEN));
    }

    @Test
    void  registerNullPlayer() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(null);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/transaction-registration")
                        .content(mapper.writeValueAsString(TransactionTestData.TRANSACTION_DTO_3))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(TransactionController.NOT_LOGGED_IN));
    }

    @Test
    void  registerBadToken() throws Exception {
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getCurrentPlayer).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
        CURRENT_PLAYER_MOCKED_STATIC.when(CurrentPlayer::getToken).thenReturn(AbstractServiceTest.TOKEN);

        mvc.perform(post("/transaction-registration")
                        .content(mapper.writeValueAsString(TransactionTestData.TRANSACTION_TO_WITH_OTHER_TOKEN))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(TransactionController.NOT_LOGGED_IN));
    }

    @Test
    void registerBadRequest() throws Exception {
        when(validator.getResult()).thenReturn(validationMessages);

        mvc.perform(post("/transaction-registration")
                        .content(mapper.writeValueAsString(TransactionTestData.TRANSACTION_DTO_3_EMPTY_OPERATION))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.operation").value(validationMessages.get("operation").get(0)));
    }

    @Test
    void processProcessed() throws Exception {
        when(transactionService.getNotProcessed()).thenReturn(Collections.singletonList(TransactionTestData.TRANSACTION_4));
        when(processing.debit(TransactionTestData.TRANSACTION_4)).thenReturn(true);

        mvc.perform(get("/process"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(TransactionController.TRANSACTION_PROCESSED));
    }

    @Test
    void processNoTransactions() throws Exception {
        when(transactionService.getNotProcessed()).thenReturn(Collections.emptyList());

        mvc.perform(get("/process"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(jsonPath("$.message").value(TransactionController.NO_TRANSACTIONS_TO_PROCESS));
    }
}
