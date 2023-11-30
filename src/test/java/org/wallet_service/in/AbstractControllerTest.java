package org.wallet_service.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.wallet_service.util.CurrentPlayer;
import org.wallet_service.util.JWT;
import org.wallet_service.validator.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractControllerTest {
    protected MockMvc mvc;
    protected final ObjectMapper mapper = new ObjectMapper();
    protected final Map<String, List<String>> validationMessages = new HashMap<>();

    @Mock
    protected Validator validator;

    protected static final MockedStatic<CurrentPlayer> CURRENT_PLAYER_MOCKED_STATIC;
    protected static final MockedStatic<JWT> JWT_MOCKED_STATIC;

    static {
        CURRENT_PLAYER_MOCKED_STATIC = mockStatic(CurrentPlayer.class);
        JWT_MOCKED_STATIC = mockStatic(JWT.class);
    }
}
