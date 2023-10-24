package org.wallet_service.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.wallet_service.PlayerTestData;
import org.wallet_service.controller.PlayerController;
import org.wallet_service.exception.MessageException;
import org.wallet_service.in.FullLogServlet;
import org.wallet_service.util.Beans;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FullLogServletTest {

    @Test
    public void test() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PlayerController playerController = mock(PlayerController.class);
        ObjectMapper objectMapper = new ObjectMapper();

        MockedStatic<Beans> beans = mockStatic(Beans.class);
        beans.when(Beans::getPlayerController).thenReturn(playerController);
        beans.when(Beans::getObjectMapper).thenReturn(objectMapper);

        FullLogServlet fullLogServlet = new FullLogServlet();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        String json = "{\"playerId\":" + 1 + "}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        when(playerController.getFullLog(1)).thenReturn(PlayerTestData.FULL_PLAYER_ACTIONS);

        fullLogServlet.doPost(request, response);

        verify(response).setStatus(200);
        verify(response).setContentType("application/json");

        String result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(PlayerTestData.FULL_PLAYER_ACTIONS_RESPONSE));

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        json = "{\"playerId\":" + 4 + "}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(printWriter);

        when(playerController.getFullLog(4)).thenThrow(new MessageException("Игрок с таким id не найден"));

        fullLogServlet.doPost(request, response);

        result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"Игрок с таким id не найден\"}");

        verify(response).setStatus(404);
    }

}
