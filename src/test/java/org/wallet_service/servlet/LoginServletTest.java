//package org.wallet_service.servlet;
//
//import jakarta.servlet.ServletConfig;
//import org.junit.Test;
//import org.wallet_service.AbstractServiceTest;
//import org.wallet_service.AbstractServletTest;
//import org.wallet_service.PlayerTestData;
//import org.wallet_service.entity.Player;
//import org.wallet_service.exception.AuthenticationException;
//import org.wallet_service.in.LoginServlet;
//import org.wallet_service.util.CurrentPlayer;
//
//import java.io.BufferedReader;
//import java.io.PrintWriter;
//import java.io.StringReader;
//import java.io.StringWriter;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//public class LoginServletTest extends AbstractServletTest {
//
//    @Test
//    public void test() throws Exception {
//        LoginServlet loginServlet = new LoginServlet(){
//            @Override
//            public ServletConfig getServletConfig() {
//                return servletConfig;
//            }
//        };
//        when(loginServlet.getServletConfig().getServletContext()).thenReturn(servletContext);
//
//        Player player = PlayerTestData.PLAYER_1_WITH_ID;
//        String email = player.getEmail();
//        String password = player.getPassword();
//
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//
//        when(response.getWriter()).thenReturn(printWriter);
//
//        String json = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
//
//        when(playerController.login(email, password)).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);
//
//        CurrentPlayer.setToken(AbstractServiceTest.TOKEN);
//
//        loginServlet.doPost(request, response);
//
//        verify(response, atLeast(1)).setStatus(200);
//        verify(response, atLeast(1)).setContentType("application/json");
//
//        String result = stringWriter.getBuffer().toString().trim();
//
//        assertThat(result).isEqualTo(objectMapper.writeValueAsString(PlayerTestData.PLAYER_RESPONSE_TO));
//
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
//        verify(response, atLeast(1)).setStatus(401);
//    }
//}
