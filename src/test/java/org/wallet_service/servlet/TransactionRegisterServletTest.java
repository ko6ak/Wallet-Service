package org.wallet_service.servlet;

import jakarta.servlet.ServletConfig;
import org.junit.Test;
import org.wallet_service.AbstractServletTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.TransactionTO;
import org.wallet_service.TransactionTestData;
import org.wallet_service.in.TransactionController;
import org.wallet_service.entity.Operation;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.in.TransactionRegisterServlet;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TransactionRegisterServletTest extends AbstractServletTest {

    @Test
    public void test() throws Exception {
        TransactionRegisterServlet transactionRegisterServlet = new TransactionRegisterServlet(){
            @Override
            public ServletConfig getServletConfig() {
                return servletConfig;
            }
        };
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("player")).thenReturn(PlayerTestData.PLAYER_1_WITH_ID);

        TransactionTO transactionTO = TransactionTestData.TRANSACTION_TO_1;
        String id = transactionTO.getId().toString();
        String operation = String.valueOf(transactionTO.getOperation());
        String amount = transactionTO.getAmount();
        String description = transactionTO.getDescription();
        String token = transactionTO.getToken();

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        String json = "{\"id\":\"" + id + "\", \"operation\":\"" + operation + "\", \"amount\":\"" + amount +
                "\", \"description\":\"" + description + "\", \"token\":\"" + token + "\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        when(transactionController.register(UUID.fromString(id), Operation.valueOf(operation), amount, description, token))
                .thenReturn(TransactionController.REGISTER_SUCCESS);

        when(validator.getResult()).thenReturn(new HashMap<>());

        transactionRegisterServlet.doPost(request, response);

        verify(response, atLeast(1)).setStatus(200);
        verify(response, atLeast(1)).setContentType("application/json");

        String result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"" + TransactionController.REGISTER_SUCCESS + "\"}");

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(printWriter);

        when(transactionController.register(UUID.fromString(id), Operation.valueOf(operation), amount, description, token))
                .thenThrow(new AuthenticationException("Сначала нужно залогинится"));

        transactionRegisterServlet.doPost(request, response);

        result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"Сначала нужно залогинится\"}");

        verify(response).setStatus(401);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(response.getWriter()).thenReturn(printWriter);

        TransactionTO transactionTO_1 = TransactionTestData.TRANSACTION_TO_2;
        id = transactionTO_1.getId().toString();
        operation = String.valueOf(transactionTO_1.getOperation());
        amount = transactionTO_1.getAmount();
        description = transactionTO_1.getDescription();
        token = transactionTO_1.getToken();

        json = "{\"id\":\"" + id + "\", \"operation\":\"" + operation + "\", \"amount\":\"" + amount +
                "\", \"description\":\"" + description + "\", \"token\":\"" + token + "\"}";
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        when(transactionController.register(UUID.fromString(id), Operation.valueOf(operation), amount, description, token))
                .thenThrow(new TransactionException("Не уникальный id транзакции"));

        transactionRegisterServlet.doPost(request, response);

        result = stringWriter.getBuffer().toString().trim();

        assertThat(result).isEqualTo("{\"message\":\"Не уникальный id транзакции\"}");

        verify(response, atLeast(1)).setStatus(400);
    }
}
