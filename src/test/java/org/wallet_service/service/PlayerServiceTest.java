package org.wallet_service.service;

import org.junit.jupiter.api.Test;
import org.wallet_service.AbstractServiceTest;
import org.wallet_service.PlayerTestData;
import org.wallet_service.entity.Player;
import org.wallet_service.util.Beans;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.wallet_service.util.DBConnection.CONNECTION;

public class PlayerServiceTest extends AbstractServiceTest {
    private static final PlayerService playerService = Beans.getPlayerService();
    private static final MoneyAccountService moneyAccountService = Beans.getMoneyAccountService();

    @Test
    void save() throws SQLException {
        moneyAccountService.save(PlayerTestData.ACCOUNT_1002_WITHOUT_ID);
        Player player = playerService.save(PlayerTestData.PLAYER_2_WITHOUT_ID);
        assertThat(player).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_2_WITH_ID);
        CONNECTION.createStatement().executeUpdate("DELETE FROM wallet.player WHERE id = 2");
    }

    @Test
    void getWithId(){
        Player player = playerService.get(1);
        assertThat(player).isNotNull();
        assertThat(player).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_1_WITH_ID);
        assertThat(playerService.get(4)).isNull();
    }

    @Test
    void getWithLoginAndPassword(){
        Player player = playerService.get(PlayerTestData.PLAYER_1_WITH_ID.getEmail(), PlayerTestData.PLAYER_1_WITH_ID.getPassword());
        assertThat(player).isNotNull();
        assertThat(player).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_1_WITH_ID);
    }

    @Test
    void isFound(){
        assertThat(playerService.isFound(PlayerTestData.PLAYER_1_WITH_ID.getEmail())).isTrue();
    }
}
