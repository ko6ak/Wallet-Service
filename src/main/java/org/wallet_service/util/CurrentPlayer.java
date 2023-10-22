package org.wallet_service.util;

import org.wallet_service.entity.Player;

public final class CurrentPlayer {
    private static Player currentPlayer;

    private CurrentPlayer() {
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }
}
