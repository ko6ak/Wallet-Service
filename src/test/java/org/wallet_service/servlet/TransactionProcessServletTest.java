//package org.wallet_service.servlet;
//
//import org.junit.Test;
//import org.mockito.MockedStatic;
//import org.wallet_service.AbstractServletTest;
//import org.wallet_service.exception.TransactionException;
//import org.wallet_service.in.TransactionProcessServlet;
//import org.wallet_service.util.Processing;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//public class TransactionProcessServletTest extends AbstractServletTest {
//
//    @Test
//    public void test() throws Exception {
//        MockedStatic<Processing> processingMockedStatic = mockStatic(Processing.class);
//        processingMockedStatic.when(Processing::process).thenReturn("Все транзакции обработаны");
//
//        TransactionProcessServlet transactionProcessServlet = new TransactionProcessServlet();
//
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(stringWriter);
//
//        when(response.getWriter()).thenReturn(printWriter);
//
//        transactionProcessServlet.doGet(request, response);
//
//        verify(response, atLeast(1)).setStatus(200);
//        verify(response, atLeast(1)).setContentType("application/json");
//
//        String result = stringWriter.getBuffer().toString().trim();
//
//        assertThat(result).isEqualTo("{\"message\":\"Все транзакции обработаны\"}");
//
//        stringWriter = new StringWriter();
//        printWriter = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(printWriter);
//
//        processingMockedStatic.when(Processing::process).thenThrow(new TransactionException("Баланс меньше списываемой суммы"));
//
//        transactionProcessServlet.doGet(request, response);
//
//        result = stringWriter.getBuffer().toString().trim();
//
//        assertThat(result).isEqualTo("{\"message\":\"Баланс меньше списываемой суммы\"}");
//
//        verify(response, atLeast(1)).setStatus(400);
//    }
//
//}
