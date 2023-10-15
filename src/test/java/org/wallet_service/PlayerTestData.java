//package org.wallet_service;
//
//import org.wallet_service.dto.PlayerTO;
//import org.wallet_service.entity.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.*;
//
//public class PlayerTestData {
//    public static final MoneyAccount ACCOUNT_WITHOUT_ID;
//    public static final MoneyAccount ACCOUNT_WITH_ID;
//    public static final MoneyAccount ACCOUNT_1;
//    public static final MoneyAccount ACCOUNT_2;
//    public static final MoneyAccount ACCOUNT_FOR_PLAYER_1;
//
//    public static Player PLAYER_1;
//    public static Player PLAYER_WITHOUT_ID;
//    public static Player PLAYER_WITH_ID;
//
//    public static PlayerTO PLAYER_TO;
//    public static PlayerTO PLAYER_TO_WITH_BAD_LOGIN;
//    public static PlayerTO PLAYER_TO_WITH_BAD_PASSWORD;
//
//    public static final Action ACTION_1 = new Action(LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 10, 30), "Успешная регистрация");
//    public static final Action ACTION_2 = new Action(LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 11, 40), "Успешный вход");
//    public static final Action ACTION_3 = new Action(LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 12, 50), "Успешный выход");
//    public static final Action ACTION_4 = new Action(LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 16, 35), "Успешная регистрация");
//
//    public static final Action CREATED_TRANSACTION_ACTION_1 = new Action(LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 11, 50),
//            "Создана транзакция с типом операции CREDIT, суммой 1000.00 и комментарием 'transaction #1'");
//    public static final Action CREATED_TRANSACTION_ACTION_2 = new Action(LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 12, 5),
//            "Создана транзакция с типом операции DEBIT, суммой 699.99 и комментарием 'transaction #2'");
//
//    public static final Action PROCESSED_TRANSACTION_ACTION_1 = new Action(LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 12, 15),
//            "Транзакция с типом операции CREDIT, суммой 1000.00 и комментарием 'transaction #1' успешно выполнена");
//    public static final Action PROCESSED_TRANSACTION_ACTION_2 = new Action(LocalDateTime.of(2023, Month.OCTOBER, 9, 17, 12, 18),
//            "Транзакция с типом операции DEBIT, суммой 699.99 и комментарием 'transaction #2' успешно выполнена");
//
//    public static final List<Action> ACTIONS_1 = new ArrayList<>();
//    public static final List<Action> ACTIONS_2 = new ArrayList<>();
//    public static final List<Action> TRANSACTION_ACTIONS = new ArrayList<>();
//    public static final List<Action> FULL_ACTIONS = new ArrayList<>();
//
//    static {
//        ACCOUNT_WITHOUT_ID = new MoneyAccount(new BigDecimal("0.00"));
//
//        ACCOUNT_WITH_ID = new MoneyAccount(new BigDecimal("0.00"));
//        ACCOUNT_WITH_ID.setId(1001);
//
//        ACCOUNT_1 = new MoneyAccount(new BigDecimal("0.00"));
//        PLAYER_WITHOUT_ID = new Player("Петр", "petr@ya.ru", "4321", ACCOUNT_1);
//
//        ACCOUNT_2 = new MoneyAccount(new BigDecimal("0.00"));
//        PLAYER_WITH_ID = new Player("Петр", "petr@ya.ru", "4321", ACCOUNT_2);
//        PLAYER_WITH_ID.setId(1);
//
//        ACCOUNT_FOR_PLAYER_1 = new MoneyAccount(new BigDecimal("0.00"));
//        ACCOUNT_FOR_PLAYER_1.setId(1001);
//        PLAYER_1 = new Player("Иван", "ivan@ya.ru", "1234", ACCOUNT_FOR_PLAYER_1);
//        PLAYER_1.setId(1);
////        ACCOUNT_FOR_PLAYER_1.setPlayerId(1);
//
//        PLAYER_TO = new PlayerTO("Иван", "ivan@ya.ru", "1234");
//        PLAYER_TO_WITH_BAD_LOGIN = new PlayerTO("Иван", "ivashka@ya.ru", "1234");
//        PLAYER_TO_WITH_BAD_PASSWORD = new PlayerTO("Иван", "ivan@ya.ru", "1234567");
//
//        Collections.addAll(ACTIONS_1, ACTION_1, ACTION_2, ACTION_3);
//        Collections.addAll(ACTIONS_2, ACTION_4);
//        Collections.addAll(TRANSACTION_ACTIONS, PROCESSED_TRANSACTION_ACTION_1, PROCESSED_TRANSACTION_ACTION_2);
//        FULL_ACTIONS.addAll(TRANSACTION_ACTIONS);
//        FULL_ACTIONS.addAll(ACTIONS_1);
//        Collections.addAll(FULL_ACTIONS, CREATED_TRANSACTION_ACTION_1, CREATED_TRANSACTION_ACTION_2);
//        FULL_ACTIONS.sort(Comparator.comparing(Action::getDateTime));
//    }
//}
