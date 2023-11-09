//package org.wallet_service.servlet;
//
//import org.junit.Test;
//import org.wallet_service.AbstractServletTest;
//import org.wallet_service.PlayerTestData;
//import org.wallet_service.entity.Player;
//import org.wallet_service.exception.AuthenticationException;
//import org.wallet_service.in.RegistrationServlet;
//
//import java.io.BufferedReader;
//import java.io.PrintWriter;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.util.HashMap;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//public class RegistrationServletTest extends AbstractServletTest {
//
//    @Test
//    public void test() throws Exception {
//        RegistrationServlet registrationServlet = new RegistrationServlet();
//
//        Player player = PlayerTestData.PLAYER_1_WITH_ID;
//        String name = player.getName();
//        String email = player.getEmail();
//        String password = player.getPassword();
//
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//
//        when(response.getWriter()).thenReturn(printWriter);
//
//        String json = "{\"name\":\"" + name + "\", \"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
//
//        when(playerController.registration(name, email, password)).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
//
//        when(validator.getResult()).thenReturn(new HashMap<>());
//
//        registrationServlet.doPost(request, response);
//
//        verify(response, atLeast(1)).setStatus(201);
//        verify(response, atLeast(1)).setContentType("application/json");
//
//        String result = stringWriter.getBuffer().toString().trim();
//
//        assertThat(result).isEqualTo(objectMapper.writeValueAsString(PlayerTestData.PLAYER_RESPONSE_TO_WITHOUT_TOKEN));
//
//        stringWriter = new StringWriter();
//        printWriter = new PrintWriter(stringWriter);
//
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
//        when(response.getWriter()).thenReturn(printWriter);
//
//        when(playerController.registration(name, email, password)).thenThrow(new AuthenticationException("Игрок с таким email уже есть в системе"));
//
//        registrationServlet.doPost(request, response);
//
//        result = stringWriter.getBuffer().toString().trim();
//
//        assertThat(result).isEqualTo("{\"message\":\"Игрок с таким email уже есть в системе\"}");
//
//        verify(response, atLeast(1)).setStatus(401);
//    }
//}
