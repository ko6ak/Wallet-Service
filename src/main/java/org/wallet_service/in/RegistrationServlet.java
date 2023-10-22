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
import org.wallet_service.controller.PlayerController;
import org.wallet_service.dto.response.MessageResponseTO;
import org.wallet_service.dto.PlayerTO;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class RegistrationServlet extends HttpServlet {
    private static final PlayerController playerController = Beans.getPlayerController();
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

        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = req.getReader()) {
            String input;
            while ((input = reader.readLine()) != null) {
                sb.append(input).append('\n');
            }
        }

        JsonNode jsonNode = mapper.readTree(sb.toString());
        String name = jsonNode.get("name").asText();
        String login = jsonNode.get("email").asText();
        String password = jsonNode.get("password").asText();

        PlayerTO playerTO = new PlayerTO(name, login, password);

        Set<ConstraintViolation<PlayerTO>> violations = validator.validate(playerTO);

        if (!violations.isEmpty()){
            Map<String, String> errors = new LinkedHashMap<>();
            for (ConstraintViolation<PlayerTO> cv : violations){
                errors.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), errors);
        }

        try{
            mapper.writeValue(resp.getWriter(), playerController.registration(playerTO));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
        catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }
}
