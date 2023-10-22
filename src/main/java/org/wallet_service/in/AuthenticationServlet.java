package org.wallet_service.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wallet_service.controller.PlayerController;
import org.wallet_service.dto.response.MessageResponseTO;
import org.wallet_service.dto.response.PlayerResponseTO;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.mapper.PlayerResponseMapper;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;

import java.io.BufferedReader;
import java.io.IOException;

public class AuthenticationServlet extends HttpServlet {
    private static final PlayerController playerController = Beans.getPlayerController();
    private static final ObjectMapper mapper = Beans.getObjectMapper();

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
        String email = jsonNode.get("email").asText();
        String password = jsonNode.get("password").asText();

        try{
            Player player = playerController.login(email, password);
            getServletConfig().getServletContext().setAttribute("player", player);
            PlayerResponseTO playerResponseTO = PlayerResponseMapper.INSTANCE.playerToPlayerResponseTO(player);
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), playerResponseTO);
        }
        catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try{
            getServletConfig().getServletContext().setAttribute("player", null);
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(playerController.logout()));
        }
        catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }
}
