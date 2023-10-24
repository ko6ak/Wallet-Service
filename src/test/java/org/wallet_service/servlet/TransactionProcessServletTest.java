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
import org.wallet_service.exception.TransactionException;
import org.wallet_service.in.BalanceServlet;
import org.wallet_service.in.TransactionProcessServlet;
import org.wallet_service.util.Beans;
import org.wallet_service.util.CurrentPlayer;
import org.wallet_service.util.Processing;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TransactionProcessServletTest {

    @Test
    public void test() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        MockedStatic<Processing> processingMockedStatic = mockStatic(Processing.class);
        processingMockedStatic.when(Processing::process).thenReturn("Все транзакции обработаны");

        TransactionProcessServlet transactionProcessServlet = new TransactionProcessServlet();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        transactionProcessServlet.doGet(request, response);

        verify(response).setStatus(200);
        verify(response).setContentType("application/json");

        String result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"Все транзакции обработаны\"}");

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        processingMockedStatic.when(Processing::process).thenThrow(new TransactionException("Баланс меньше списываемой суммы"));

        transactionProcessServlet.doGet(request, response);

        result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"Баланс меньше списываемой суммы\"}");

        verify(response).setStatus(400);
    }

}
