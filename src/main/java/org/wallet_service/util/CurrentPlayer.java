package org.wallet_service.util;

import org.wallet_service.entity.Player;

public final class CurrentPlayer {
    private static Player currentPlayer;
    private static String token;

    private CurrentPlayer() {
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        CurrentPlayer.token = token;
    }
}
