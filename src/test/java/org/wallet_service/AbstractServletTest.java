package org.wallet_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.MockedStatic;
import org.wallet_service.in.PlayerController;
import org.wallet_service.in.TransactionController;
import org.wallet_service.util.Beans;
import org.wallet_service.validator.Validator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public abstract class AbstractServletTest {
    public static HttpServletRequest request = mock(HttpServletRequest.class);
    public static HttpServletResponse response = mock(HttpServletResponse.class);
    public static ServletContext servletContext = mock(ServletContext.class);
    public static ServletConfig servletConfig = mock(ServletConfig.class);
    public static PlayerController playerController = mock(PlayerController.class);
    public static TransactionController transactionController = mock(TransactionController.class);
    public static ObjectMapper objectMapper = new ObjectMapper();
    public Validator validator = mock(Validator.class);

    static {
        MockedStatic<Beans> beans = mockStatic(Beans.class);
        beans.when(Beans::getPlayerController).thenReturn(playerController);
        beans.when(Beans::getObjectMapper).thenReturn(objectMapper);
        beans.when(Beans::getTransactionController).thenReturn(transactionController);
    }
}
