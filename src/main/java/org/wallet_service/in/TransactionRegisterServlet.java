package org.wallet_service.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wallet_service.controller.TransactionController;
import org.wallet_service.dto.MessageResponseTO;
import org.wallet_service.entity.Operation;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;
import org.wallet_service.util.CurrentPlayer;
import org.wallet_service.util.Validator;

import java.io.IOException;
import java.util.*;

import static org.wallet_service.util.Util.getJSONFromRequest;

/**
 * Сервлет обслуживающий запрос регистрации транзакции.
 */
public class TransactionRegisterServlet extends HttpServlet {
    private static final TransactionController transactionController = Beans.getTransactionController();
    private static final ObjectMapper mapper = Beans.getObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (ConfigParser.configIsNull()) ConfigParser.parse();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        JsonNode jsonNode = mapper.readTree(getJSONFromRequest(req));
        String id = jsonNode.get("id").asText();
        String operation = jsonNode.get("operation").asText();
        String amount = jsonNode.get("amount").asText();
        String description = jsonNode.get("description").asText();
        String token = jsonNode.get("token").asText();

        Validator validator = new Validator();

        Map<String, String> notBlank = new HashMap<>();
        notBlank.put("description", description);
        notBlank.put("token", token);
        notBlank.put("id", id);
        notBlank.put("operation", operation);

        Map<String, String> checkAmount = new HashMap<>();
        checkAmount.put("amount", amount);

        validator.checkNotBlank(notBlank);
        validator.checkFormat(checkAmount);
        validator.checkMinDecimal(checkAmount);

        if (!validator.getResult().isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), validator.getResult());
            return;
        }

        CurrentPlayer.setCurrentPlayer((Player) getServletConfig().getServletContext().getAttribute("player"));

        try{
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(transactionController.register(
                    UUID.fromString(id), Operation.valueOf(operation), amount, description, token)));
        }
        catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
        catch (TransactionException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }
}
