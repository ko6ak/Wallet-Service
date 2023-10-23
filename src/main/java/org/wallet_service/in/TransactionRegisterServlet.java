package org.wallet_service.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.wallet_service.controller.TransactionController;
import org.wallet_service.dto.TransactionTO;
import org.wallet_service.dto.response.MessageResponseTO;
import org.wallet_service.entity.Operation;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;
import org.wallet_service.util.CurrentPlayer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.wallet_service.util.Util.getJSONFromRequest;

/**
 * Сервлет обслуживающий запрос регистрации транзакции.
 */
public class TransactionRegisterServlet extends HttpServlet {
    private static final TransactionController transactionController = Beans.getTransactionController();
    private static final ObjectMapper mapper = Beans.getObjectMapper();

    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (ConfigParser.configIsNull()) ConfigParser.parse();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        JsonNode jsonNode = mapper.readTree(getJSONFromRequest(req));
        String id = jsonNode.get("id").asText();
        String operation = jsonNode.get("operation").asText();
        String amount = jsonNode.get("amount").asText();
        String description = jsonNode.get("description").asText();
        String token = jsonNode.get("token").asText();

        TransactionTO transactionTO = new TransactionTO(UUID.fromString(id),
                Operation.valueOf(operation),
                amount,
                description,
                token);

        Set<ConstraintViolation<TransactionTO>> violations = validator.validate(transactionTO);

        if (!violations.isEmpty()){
            Map<String, String> errors = new LinkedHashMap<>();
            for (ConstraintViolation<TransactionTO> cv : violations){
                errors.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), errors);
        }

        CurrentPlayer.setCurrentPlayer((Player) getServletConfig().getServletContext().getAttribute("player"));

        try{
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(transactionController.register(transactionTO)));
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
