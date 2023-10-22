package org.wallet_service.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wallet_service.controller.PlayerController;
import org.wallet_service.dto.response.ActionResponseTO;
import org.wallet_service.dto.response.MessageResponseTO;
import org.wallet_service.entity.Action;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.MessageException;
import org.wallet_service.mapper.ActionResponseMapper;
import org.wallet_service.util.Beans;
import org.wallet_service.util.ConfigParser;
import org.wallet_service.util.CurrentPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogServlet extends HttpServlet {
    private static final PlayerController playerController = Beans.getPlayerController();
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
            CurrentPlayer.setCurrentPlayer((Player) getServletConfig().getServletContext().getAttribute("player"));

            List<Action> actions = playerController.getTransactionLog();
            List<ActionResponseTO> actionResponseTOs = new ArrayList<>();
            actions.forEach(a -> actionResponseTOs.add(ActionResponseMapper.INSTANCE.ActionToActionResponseTO(a)));

            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), actionResponseTOs);
        }
        catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
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
        long playerId = jsonNode.get("playerId").asLong();

        try{
            List<Action> actions = playerController.getFullLog(playerId);
            List<ActionResponseTO> actionResponseTOs = new ArrayList<>();
            actions.forEach(a -> actionResponseTOs.add(ActionResponseMapper.INSTANCE.ActionToActionResponseTO(a)));

            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), actionResponseTOs);
        }
        catch (MessageException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }
}
