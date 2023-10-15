//package org.wallet_service.service;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.wallet_service.PlayerTestData;
//import org.wallet_service.entity.MoneyAccount;
//import org.wallet_service.util.Beans;
//import org.wallet_service.util.ConfigParser;
//import org.wallet_service.util.DBConnection;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class MoneyAccountServiceTest {
//    private static final MoneyAccountService moneyAccountService = Beans.getMoneyAccountService();
//
//    @BeforeAll
//    static void init() {
//        ConfigParser.parse();
//        DBConnection.updateTables();
//    }
////
////    @AfterAll
////    static void clearAll() {
////        moneyAccountService.clear();
////    }
//
//    @Test
//    void save(){
//        MoneyAccount moneyAccount = moneyAccountService.save(PlayerTestData.ACCOUNT_WITHOUT_ID);
//        System.out.println(moneyAccount);
////        assertThat(moneyAccount).usingRecursiveComparison().isEqualTo(PlayerTestData.ACCOUNT_WITH_ID);
//    }
//
//    @Test
//    void get(){
////        moneyAccountService.save(PlayerTestData.ACCOUNT_WITHOUT_ID);
//        MoneyAccount moneyAccount = moneyAccountService.get(PlayerTestData.ACCOUNT_WITH_ID.getId());
//        System.out.println(moneyAccount);
////        assertThat(moneyAccount).isNotNull();
////        assertThat(moneyAccount).usingRecursiveComparison().isEqualTo(PlayerTestData.ACCOUNT_WITH_ID);
//    }
//}
