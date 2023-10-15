//package org.wallet_service.service;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.wallet_service.PlayerTestData;
//import org.wallet_service.util.Beans;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class PlayerActionServiceTest {
//    private static final PlayerActionService playerActionService = Beans.getPlayerActionService();
//
////    @BeforeEach
////    void clear() {
////        playerActionService.clear();
////        PlayerTestData.ACTIONS_1.forEach(a -> playerActionService.add(1, a));
////    }
//
////    @AfterAll
////    static void clearAll() {
////        playerActionService.clear();
////    }
//
//    @Test
//    void get(){
//        assertThat(playerActionService.get(1)).containsAll(PlayerTestData.ACTIONS_1).hasSize(3);
//    }
//
//    @Test
//    void add(){
//        playerActionService.add(2, PlayerTestData.ACTION_4);
//        assertThat(playerActionService.get(2)).containsAll(PlayerTestData.ACTIONS_2).hasSize(1);
//    }
//}
