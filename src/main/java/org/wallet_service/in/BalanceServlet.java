package org.wallet_service.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wallet_service.controller.PlayerController;
import org.wallet_service.dto.response.MessageResponseTO;
import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.util.Beans;
import org.wallet_service.util.CurrentPlayer;

import java.io.IOException;

public class BalanceServlet extends HttpServlet {
    private static final PlayerController playerController = Beans.getPlayerController();
    private static final ObjectMapper mapper = Beans.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try{
            CurrentPlayer.setCurrentPlayer((Player) getServletConfig().getServletContext().getAttribute("player"));
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(playerController.getBalance()));
        }
        catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            mapper.writeValue(resp.getWriter(), new MessageResponseTO(e.getMessage()));
        }
    }
}
