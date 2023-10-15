//package org.wallet_service.service;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.wallet_service.PlayerTestData;
//import org.wallet_service.TransactionTestData;
//import org.wallet_service.util.Beans;
//
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class TransactionServiceTest {
//    private static final TransactionService transactionService = Beans.getTransactionService();
//
////    @BeforeEach
////    void clear() {
////        transactionService.clear();
////        TransactionTestData.TRANSACTIONS.forEach(transactionService::save);
////    }
////
////    @AfterAll
////    static void clearAll() {
////        transactionService.clear();
////    }
//
//    @Test
//    void save(){
//        TransactionTestData.TRANSACTIONS_IDS.forEach(uuid -> assertThat(transactionService.isFound(uuid)).isTrue());
//    }
//
//    @Test
//    void isFound(){
//        assertThat(transactionService.isFound(TransactionTestData.TRANSACTION_1.getId())).isTrue();
//        assertThat(transactionService.isFound(UUID.fromString("4e42a3f4-dcf4-49a2-9963-c7f49ca1b146"))).isFalse();
//    }
//
//    @Test
//    void getNotProcessed(){
//        assertThat(TransactionTestData.NOT_PROCESSED_TRANSACTIONS).isEqualTo(transactionService.getNotProcessed());
//    }
//}
