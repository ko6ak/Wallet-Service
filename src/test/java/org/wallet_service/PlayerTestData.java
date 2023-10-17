package org.wallet_service;

import org.wallet_service.dto.PlayerTO;
import org.wallet_service.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class PlayerTestData {
    public static final MoneyAccount ACCOUNT_WITHOUT_ID;
    public static final MoneyAccount ACCOUNT_WITH_ID;
    public static final MoneyAccount ACCOUNT_1;
    public static final MoneyAccount ACCOUNT_2;
    public static final MoneyAccount ACCOUNT_FOR_PLAYER_1;

    public static Player PLAYER_1;
    public static Player PLAYER_WITHOUT_ID;
    public static Player PLAYER_WITH_ID;

    public static PlayerTO PLAYER_TO;
    public static PlayerTO PLAYER_TO_WITH_BAD_LOGIN;
    public static PlayerTO PLAYER_TO_WITH_BAD_PASSWORD;

    public static final PlayerAction PLAYER_ACTION_1 = new PlayerAction(1, 1, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 10, 10), "Успешная регистрация");
    public static final PlayerAction PLAYER_ACTION_2 = new PlayerAction(2, 1, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 11, 10), "Успешный вход");
    public static final PlayerAction PLAYER_ACTION_3 = new PlayerAction(5, 1, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 15, 10), "Успешный выход");
    public static final PlayerAction PLAYER_ACTION_WITHOUT_ID = new PlayerAction(1, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 23, 35), "Успешный вход");
    public static final PlayerAction PLAYER_ACTION_WITH_ID = new PlayerAction(6, 1, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 23, 35), "Успешный вход");

    public static final PlayerAction CREATED_TRANSACTION_PLAYER_ACTION_1 = new PlayerAction(3, 1, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 12, 10),
            "Создана транзакция с типом операции CREDIT, суммой 1000.00 и комментарием 'transaction #1'");
    public static final PlayerAction CREATED_TRANSACTION_PLAYER_ACTION_2 = new PlayerAction(4, 1, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 13, 10),
            "Создана транзакция с типом операции DEBIT, суммой 699.99 и комментарием 'transaction #2'");

    public static final MoneyAccountAction MONEY_ACCOUNT_ACTION_WITH_ID_1 = new MoneyAccountAction(1, 1001, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 10),
            "Транзакция с типом операции CREDIT, суммой 1000.00 и комментарием 'transaction #1' успешно выполнена");
    public static final MoneyAccountAction MONEY_ACCOUNT_ACTION_WITH_ID_2 = new MoneyAccountAction(2, 1001, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 20),
            "Транзакция с типом операции DEBIT, суммой 699.99 и комментарием 'transaction #2' успешно выполнена");
    public static final MoneyAccountAction MONEY_ACCOUNT_ACTION_WITH_ID_3 = new MoneyAccountAction(3, 1001, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 30),
            "Транзакция с типом операции DEBIT, суммой 400.00 и комментарием 'transaction #3' не выполнена. Причина: Баланс меньше списываемой суммы");

    public static final MoneyAccountAction MONEY_ACCOUNT_ACTION_WITHOUT_ID_1 = new MoneyAccountAction(1001, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 10),
            "Транзакция с типом операции CREDIT, суммой 1000.00 и комментарием 'transaction #1' успешно выполнена");
    public static final MoneyAccountAction MONEY_ACCOUNT_ACTION_WITHOUT_ID_2 = new MoneyAccountAction(1001, LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 20),
            "Транзакция с типом операции DEBIT, суммой 699.99 и комментарием 'transaction #2' успешно выполнена");

    public static final List<PlayerAction> ACTIONS_1 = new ArrayList<>();
    public static final List<PlayerAction> ACTIONS_2 = new ArrayList<>();
    public static final List<MoneyAccountAction> MONEY_ACCOUNT_ACTIONS = new ArrayList<>();
    public static final List<MoneyAccountAction> MONEY_ACCOUNT_ACTIONS_FULL = new ArrayList<>();
    public static final List<Action> FULL_PLAYER_ACTIONS = new ArrayList<>();

    static {
        ACCOUNT_WITHOUT_ID = new MoneyAccount(new BigDecimal("0.00"));

        ACCOUNT_WITH_ID = new MoneyAccount(1001, new BigDecimal("0.00"));

        ACCOUNT_1 = new MoneyAccount(new BigDecimal("0.00"));
        PLAYER_WITHOUT_ID = new Player("Петр", "petr@ya.ru", "4321", ACCOUNT_1);

        ACCOUNT_2 = new MoneyAccount(new BigDecimal("0.00"));
        PLAYER_WITH_ID = new Player("Петр", "petr@ya.ru", "4321", ACCOUNT_2);
        PLAYER_WITH_ID.setId(1);

        ACCOUNT_FOR_PLAYER_1 = new MoneyAccount(1001, new BigDecimal("0.00"));
        PLAYER_1 = new Player("Иван", "ivan@ya.ru", "1234", ACCOUNT_FOR_PLAYER_1);
        PLAYER_1.setId(1);

        PLAYER_TO = new PlayerTO("Иван", "ivan@ya.ru", "1234");
        PLAYER_TO_WITH_BAD_LOGIN = new PlayerTO("Иван", "ivashka@ya.ru", "1234");
        PLAYER_TO_WITH_BAD_PASSWORD = new PlayerTO("Иван", "ivan@ya.ru", "1234567");

        Collections.addAll(ACTIONS_1, PLAYER_ACTION_1, PLAYER_ACTION_2, PLAYER_ACTION_3, CREATED_TRANSACTION_PLAYER_ACTION_1, CREATED_TRANSACTION_PLAYER_ACTION_2);
        ACTIONS_1.sort(Comparator.comparing(Action::getDateTime));
        ACTIONS_2.addAll(ACTIONS_1);
//        ACTIONS_2.addAll(PLAYER_ACTION_WITHOUT_ID);

        Collections.addAll(MONEY_ACCOUNT_ACTIONS, MONEY_ACCOUNT_ACTION_WITH_ID_1,
                MONEY_ACCOUNT_ACTION_WITH_ID_2);
        MONEY_ACCOUNT_ACTIONS_FULL.addAll(MONEY_ACCOUNT_ACTIONS);
        MONEY_ACCOUNT_ACTIONS_FULL.add(MONEY_ACCOUNT_ACTION_WITH_ID_3);

        FULL_PLAYER_ACTIONS.addAll(MONEY_ACCOUNT_ACTIONS);
        FULL_PLAYER_ACTIONS.addAll(ACTIONS_1);
        Collections.addAll(FULL_PLAYER_ACTIONS, CREATED_TRANSACTION_PLAYER_ACTION_1, CREATED_TRANSACTION_PLAYER_ACTION_2);
        FULL_PLAYER_ACTIONS.sort(Comparator.comparing(Action::getDateTime));
    }
}
