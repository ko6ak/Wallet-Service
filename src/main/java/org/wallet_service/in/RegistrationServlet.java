package org.wallet_service.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wallet_service.controller.PlayerController;
import org.wallet_service.dto.MessageResponseTO;
import org.wallet_service.dto.PlayerResponseTO;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.mapper.PlayerResponseMapper;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;
import org.wallet_service.util.Validator;

import java.io.IOException;
import java.util.*;

import static org.wallet_service.util.Util.getJSONFromRequest;

/**
 * Сервлет обслуживающий запрос регистрации нового Игрока.
 */
public class RegistrationServlet extends HttpServlet {
    private static final PlayerController playerController = Beans.getPlayerController();
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
        String name = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String password = jsonNode.get("password").asText();

        Validator validator = new Validator();

        Map<String, String> notBlank = new HashMap<>();
        notBlank.put("name", name);
        notBlank.put("email", email);
        notBlank.put("password", password);

        Map<String, String> sizePassword = new HashMap<>();
        sizePassword.put("password", password);

        Map<String, String> size = new HashMap<>();
        size.put("name", name);

        Map<String, String> checkEmail = new HashMap<>();
        checkEmail.put("email", email);

        validator.checkNotBlank(notBlank);
        validator.checkSizePassword(sizePassword);
        validator.checkSize(size);
        validator.checkEmail(checkEmail);

        if (!validator.getResult().isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), validator.getResult());
            return;
        }

        try{
            Player player = playerController.registration(name, email, password);
            PlayerResponseTO playerResponseTO = PlayerResponseMapper.INSTANCE.playerToPlayerResponseTO(player);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getWriter(), playerResponseTO);
        }
        catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }
}
