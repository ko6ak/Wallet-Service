package org.wallet_service.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wallet_service.dto.response.MessageResponseTO;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;
import org.wallet_service.util.Processing;

import java.io.IOException;

/**
 * Сервлет обслуживающий запрос выполнения транзакций.
 */
public class TransactionProcessServlet extends HttpServlet {
    private static final ObjectMapper mapper = Beans.getObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (ConfigParser.configIsNull()) ConfigParser.parse();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try{
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(Processing.process()));
        }
        catch (TransactionException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }
}
