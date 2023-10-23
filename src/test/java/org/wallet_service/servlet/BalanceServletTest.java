package org.wallet_service.servlet;

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.util.CurrentPlayer;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceServletTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    Servlet servlet;

    @Test
    public void test() throws Exception {
        when(request.getParameter("token")).thenReturn(AbstractServiceTest.TOKEN);

        CurrentPlayer.setToken(AbstractServiceTest.TOKEN);

//        when(request.getParameter("password")).thenReturn("passw0rd");
//        when(request.getParameter("rememberMe")).thenReturn("Y");
//        when(request.getSession()).thenReturn(session);
//        when(request.getRequestDispatcher("/HelloWorld.do")).thenReturn(rd);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        when(response.getWriter()).thenReturn(pw);

//        new Login().doPost(request, response);

        // Verify the session attribute value
//        verify(session).setAttribute("user", "abhinav");
//
//        verify(rd).forward(request, response);

        String result = sw.getBuffer().toString().trim();

        System.out.println("Result: " + result);

//        assertEquals("Login successfull...", result);
    }

}
