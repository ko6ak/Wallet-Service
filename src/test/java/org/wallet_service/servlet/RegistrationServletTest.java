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
import org.wallet_service.dto.PlayerTO;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.in.LoginServlet;
import org.wallet_service.in.RegistrationServlet;
import org.wallet_service.util.Beans;
import org.wallet_service.util.CurrentPlayer;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RegistrationServletTest {

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

//        MockedStatic<Validator> validator = mockStatic(Validator.class);
//        validator.when(Beans::getPlayerController).thenReturn(playerController);
//        validator.when(Beans::getObjectMapper).thenReturn(objectMapper);

//        LoginServlet loginServlet = new LoginServlet(){
//            @Override
//            public ServletConfig getServletConfig() {
//                return servletConfig;
//            }
//        };
//        when(loginServlet.getServletConfig().getServletContext()).thenReturn(servletContext);

        RegistrationServlet registrationServlet = new RegistrationServlet();

        Player player = PlayerTestData.PLAYER_1_WITH_ID;
        String name = player.getEmail();
        String email = player.getEmail();
        String password = player.getPassword();

        PlayerTO playerTO = new PlayerTO(name, email, password);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        String json = "{\"name\":\"" + name + "\", \"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        when(playerController.registration(playerTO)).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);

//        CurrentPlayer.setToken(AbstractServiceTest.TOKEN);


        registrationServlet.doPost(request, response);

        verify(response).setStatus(200);
        verify(response).setContentType("application/json");

        String result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo(objectMapper.writeValueAsString(PlayerTestData.PLAYER_RESPONSE_TO));

//        stringWriter = new StringWriter();
//        printWriter = new PrintWriter(stringWriter);
//
//        player = PlayerTestData.PLAYER_2_WITH_ID;
//        email = player.getEmail();
//        password = player.getPassword();
//
//        json = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
//        when(response.getWriter()).thenReturn(printWriter);
//
//        when(playerController.login(email, password)).thenThrow(new AuthenticationException("Игрок с таким email не найден"));
//
//        loginServlet.doPost(request, response);
//
//        result = stringWriter.getBuffer().toString().trim();
//
//        assertThat(result).isEqualTo("{\"message\":\"Игрок с таким email не найден\"}");
//
//        verify(response).setStatus(401);
    }
}
