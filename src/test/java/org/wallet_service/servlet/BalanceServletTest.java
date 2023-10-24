package org.wallet_service.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.controller.PlayerController;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.in.BalanceServlet;
import org.wallet_service.util.Beans;
import org.wallet_service.util.CurrentPlayer;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BalanceServletTest {

    @Test
    public void test() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletContext servletContext = mock(ServletContext.class);
        ServletConfig servletConfig = mock(ServletConfig.class);
        PlayerController playerController = mock(PlayerController.class);
        ObjectMapper objectMapper = new ObjectMapper();

        MockedStatic<Beans> beans = mockStatic(Beans.class);
        beans.when(Beans::getPlayerController).thenReturn(playerController);
        beans.when(Beans::getObjectMapper).thenReturn(objectMapper);

        String token = AbstractServiceTest.TOKEN;

        BalanceServlet balanceServlet = new BalanceServlet(){
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
        };
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("player")).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        String json = "{\"token\":\"" + token + "\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        when(playerController.getBalance(token)).thenReturn("300.01");

        balanceServlet.doPost(request, response);

        assertThat(CurrentPlayer.getCurrentPlayer()).isEqualTo(PlayerTestData.PLAYER_1_WITH_ID);

        verify(response).setStatus(200);
        verify(response).setContentType("application/json");

        String result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"300.01\"}");

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(printWriter);

        when(playerController.getBalance(token)).thenThrow(new AuthenticationException("Вы не залогинены"));

        balanceServlet.doPost(request, response);

        result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"Вы не залогинены\"}");

        verify(response).setStatus(401);
    }

}
