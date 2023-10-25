package org.wallet_service.servlet;

import jakarta.servlet.ServletConfig;
import org.junit.Test;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.AbstractServletTest;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.in.LogoutServlet;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LogoutServletTest extends AbstractServletTest {

    @Test
    public void test() throws Exception {
        String token = AbstractServiceTest.TOKEN;

        LogoutServlet logoutServlet = new LogoutServlet(){
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
        };
        when(logoutServlet.getServletConfig().getServletContext()).thenReturn(servletContext);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        String json = "{\"token\":\"" + token + "\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        when(playerController.logout(token)).thenReturn("Пока!");

        logoutServlet.doPost(request, response);

        verify(response, atLeast(1)).setStatus(200);
        verify(response, atLeast(1)).setContentType("application/json");

        String result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"Пока!\"}");

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(printWriter);

        when(playerController.logout(token)).thenThrow(new AuthenticationException("Вы не залогинены"));

        logoutServlet.doPost(request, response);

        result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"Вы не залогинены\"}");

        verify(response, atLeast(1)).setStatus(401);
    }
}
