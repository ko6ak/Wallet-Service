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
import org.wallet_service.util.CurrentPlayer;
import org.wallet_service.util.Validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.wallet_service.util.Util.getJSONFromRequest;

/**
 * Сервлет обслуживающий запрос на вход в приложение по логину и паролю.
 */
public class LoginServlet extends HttpServlet {
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
        String email = jsonNode.get("email").asText();
        String password = jsonNode.get("password").asText();

        Validator validator = new Validator();

        Map<String, String> notBlank = new HashMap<>();
        notBlank.put("email", email);
        notBlank.put("password", password);

        Map<String, String> sizePassword = new HashMap<>();
        sizePassword.put("password", password);

        Map<String, String> checkEmail = new HashMap<>();
        checkEmail.put("email", email);

        validator.checkNotBlank(notBlank);
        validator.checkSizePassword(sizePassword);
        validator.checkEmail(checkEmail);

        if (!validator.getResult().isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), validator.getResult());
            return;
        }

        try{
            Player player = playerController.login(email, password);
            getServletConfig().getServletContext().setAttribute("player", player);
            PlayerResponseTO playerResponseTO = PlayerResponseMapper.INSTANCE.playerToPlayerResponseTO(player);
            playerResponseTO.setToken(CurrentPlayer.getToken());
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), playerResponseTO);
        }
        catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }
}
