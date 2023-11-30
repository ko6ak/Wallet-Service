package org.wallet_service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wallet_service.config.TestApplicationConfig;
import org.wallet_service.dto.request.TokenRequestDTO;

import java.sql.Connection;

@Testcontainers
@WebAppConfiguration
@ContextConfiguration(classes = TestApplicationConfig.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public abstract class AbstractServiceTest {
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuQGdtYWlsLmNvbSIsImlhdCI6MTY5ODA4MDU5OSwiZXhwIjoxNzA2NzIwNTk5fQ.gbBa2D1WpLadT-bFhBLINj3aeG3VdApZfs-JiYEvFSs";
    public static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuQGdtYWlsLmNvbSIsImlhdCI6MTY5ODA4MDcxNywiZXhwIjoxNjk4MDgwNzc3fQ.2DXM5Cdb-yw6xFJJIOBfeOz3tUjl8oMM0lXQLEn5M4s";
    public static final String OTHER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuQGdtYWlsLmNvbSIsImlhdCI6MTY5ODA2OTc3MCwiZXhwIjoxNjk4MTU2MTcwfQ.luMAXp2CUiV9bzszEKNGcRYdcs3R3YXO9bwDUOHd3MI";

    public static final TokenRequestDTO TOKEN_REQUEST_DTO = new TokenRequestDTO(TOKEN);
    public static final TokenRequestDTO OTHER_TOKEN_REQUEST_DTO = new TokenRequestDTO(OTHER_TOKEN);
    public static final TokenRequestDTO EXPIRED_TOKEN_REQUEST_DTO = new TokenRequestDTO(EXPIRED_TOKEN);

    @Autowired
    protected Connection connection;
}
