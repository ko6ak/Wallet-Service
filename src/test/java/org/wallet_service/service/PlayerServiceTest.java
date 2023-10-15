//package org.wallet_service.service;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.wallet_service.PlayerTestData;
//import org.wallet_service.entity.Player;
//import org.wallet_service.util.Beans;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class PlayerServiceTest {
//    private static final PlayerService playerService = Beans.getPlayerService();
//    private static Player player;
//
//    @BeforeEach
//    void clear() {
//        playerService.clear();
//        player = playerService.save(PlayerTestData.PLAYER_WITHOUT_ID);
//    }
//
//    @AfterAll
//    static void clearAll() {
//        playerService.clear();
//    }
//
//    @Test
//    void save(){
//        assertThat(player).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_WITH_ID);
//    }
//
//    @Test
//    void getWithId(){
//        Player player = playerService.get(PlayerTestData.PLAYER_WITH_ID.getId());
//        assertThat(player).isNotNull();
//        assertThat(player).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_WITH_ID);
//        assertThat(playerService.get(4)).isNull();
//    }
//
//    @Test
//    void getWithLoginAndPassword(){
//        Optional<Player> optionalPlayer = playerService.get(PlayerTestData.PLAYER_WITH_ID.getLogin(), PlayerTestData.PLAYER_WITH_ID.getPassword());
//        Player player = optionalPlayer.get();
//        assertThat(player).isNotNull();
//        assertThat(player).usingRecursiveComparison().isEqualTo(PlayerTestData.PLAYER_WITH_ID);
//    }
//
//    @Test
//    void isFound(){
//        assertThat(playerService.isFound(PlayerTestData.PLAYER_WITH_ID.getLogin())).isTrue();
//    }
//}
