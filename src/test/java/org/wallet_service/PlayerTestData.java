package org.wallet_service;

import org.wallet_service.dto.response.ActionResponseDTO;
import org.wallet_service.dto.response.PlayerResponseDTO;
import org.wallet_service.entity.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class PlayerTestData {
    public static final MoneyAccount ACCOUNT_1002_WITHOUT_ID;
    public static final MoneyAccount ACCOUNT_1001_WITH_ID;
    public static final MoneyAccount ACCOUNT_1001;
    public static final MoneyAccount ACCOUNT_1002_WITH_ID;
    public static final MoneyAccount CHANGED_BALANCE_ACCOUNT_1001_WITH_ID;
    public static final MoneyAccount ACCOUNT_FOR_PLAYER_2;

    public static Player PLAYER_2;
    public static PlayerTO PLAYER_1_TO;
    public static Player PLAYER_2_WITHOUT_ID;
    public static Player PLAYER_2_WITH_ID;
    public static Player PLAYER_1_WITH_ID;

    public static PlayerTO PLAYER_TO;
    public static PlayerTO PLAYER_TO_WITH_BAD_LOGIN;
    public static PlayerTO PLAYER_TO_WITH_BAD_PASSWORD;

    public static PlayerResponseDTO PLAYER_RESPONSE_TO;
    public static PlayerResponseDTO PLAYER_RESPONSE_TO_WITHOUT_TOKEN;

    public static final PlayerAction PLAYER_ACTION_1 = new PlayerAction(1, 1, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 10, 10)), "Успешная регистрация");
    public static final PlayerAction PLAYER_ACTION_2 = new PlayerAction(2, 1, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 11, 10)), "Успешный вход");
    public static final PlayerAction CREATED_TRANSACTION_PLAYER_ACTION_1 = new PlayerAction(3, 1, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 12, 10)),
            "Создана транзакция с типом операции CREDIT, суммой 1000.00 и комментарием 'transaction #1'");
    public static final PlayerAction CREATED_TRANSACTION_PLAYER_ACTION_2 = new PlayerAction(4, 1, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 13, 10)),
            "Создана транзакция с типом операции DEBIT, суммой 699.99 и комментарием 'transaction #2'");
    public static final PlayerAction PLAYER_ACTION_3 = new PlayerAction(5, 1, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 15, 10)), "Успешный выход");
    public static final PlayerAction PLAYER_ACTION_WITH_ID = new PlayerAction(6, 1, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 23, 35)), "Успешный вход");

    public static final PlayerAction PLAYER_ACTION_WITHOUT_ID = new PlayerAction(1, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 23, 35)), "Успешный вход");

    public static final MoneyAccountAction MONEY_ACCOUNT_ACTION_WITH_ID_1 = new MoneyAccountAction(1, 1001, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 10)),
            "Транзакция с типом операции CREDIT, суммой 1000.00 и комментарием 'transaction #1' успешно выполнена");
    public static final MoneyAccountAction MONEY_ACCOUNT_ACTION_WITH_ID_2 = new MoneyAccountAction(2, 1001, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 20)),
            "Транзакция с типом операции DEBIT, суммой 699.99 и комментарием 'transaction #2' успешно выполнена");
    public static final MoneyAccountAction MONEY_ACCOUNT_ACTION_WITH_ID_3 = new MoneyAccountAction(3, 1001, Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 13, 9, 14, 30)),
            "Транзакция с типом операции DEBIT, суммой 400.00 и комментарием 'transaction #3' не выполнена. Причина: Баланс меньше списываемой суммы");

    public static final List<PlayerAction> ACTIONS_1 = new ArrayList<>();
    public static final List<Action> MONEY_ACCOUNT_ACTIONS = new ArrayList<>();
    public static final List<Action> MONEY_ACCOUNT_ACTIONS_FULL = new ArrayList<>();
    public static final List<Action> FULL_PLAYER_ACTIONS = new ArrayList<>();
    public static final List<ActionResponseDTO> FULL_PLAYER_ACTIONS_RESPONSE = new ArrayList<>();
    public static final List<ActionResponseDTO> MONEY_ACCOUNT_ACTIONS_RESPONSE = new ArrayList<>();

    static {
        ACCOUNT_1002_WITHOUT_ID = new MoneyAccount(new BigDecimal("1200.00"));
        ACCOUNT_1002_WITH_ID = new MoneyAccount(1002, new BigDecimal("1200.00"));

        ACCOUNT_1001_WITH_ID = new MoneyAccount(1001, new BigDecimal("300.01"));

        CHANGED_BALANCE_ACCOUNT_1001_WITH_ID = new MoneyAccount(1001, new BigDecimal("500.00"));

        ACCOUNT_1001 = new MoneyAccount(1001, new BigDecimal("300.01"));

        PLAYER_2_WITHOUT_ID = new Player("Петр", "petr@ya.ru", "54321", ACCOUNT_1002_WITH_ID);
        PLAYER_2_WITH_ID = new Player(2, "Петр", "petr@ya.ru", "54321", ACCOUNT_1002_WITH_ID);

        PLAYER_1_WITH_ID = new Player(1, "Иван", "ivan@gmail.com", "12345", ACCOUNT_1001);

        PLAYER_1_TO = new PlayerTO("Иван", "ivan@gmail.com", "12345");

        ACCOUNT_FOR_PLAYER_2 = new MoneyAccount(1002, new BigDecimal("0.00"));
        PLAYER_2 = new Player(2,"Петр", "petr@ya.ru", "54321", ACCOUNT_FOR_PLAYER_2);

        PLAYER_TO = new PlayerTO("Петр", "petr@ya.ru", "54321");
        PLAYER_TO_WITH_BAD_LOGIN = new PlayerTO("Иван", "ivashka@gmail.com", "12345");
        PLAYER_TO_WITH_BAD_PASSWORD = new PlayerTO("Иван", "ivan@gmail.com", "1234567");

        Collections.addAll(ACTIONS_1, PLAYER_ACTION_1, PLAYER_ACTION_2, PLAYER_ACTION_3, CREATED_TRANSACTION_PLAYER_ACTION_1, CREATED_TRANSACTION_PLAYER_ACTION_2);
        ACTIONS_1.sort(Comparator.comparing(Action::getDateTime));

        Collections.addAll(MONEY_ACCOUNT_ACTIONS, MONEY_ACCOUNT_ACTION_WITH_ID_1, MONEY_ACCOUNT_ACTION_WITH_ID_2);
        MONEY_ACCOUNT_ACTIONS_FULL.addAll(MONEY_ACCOUNT_ACTIONS);
        MONEY_ACCOUNT_ACTIONS_FULL.add(MONEY_ACCOUNT_ACTION_WITH_ID_3);

        FULL_PLAYER_ACTIONS.addAll(MONEY_ACCOUNT_ACTIONS);
        FULL_PLAYER_ACTIONS.addAll(ACTIONS_1);
        FULL_PLAYER_ACTIONS.sort(Comparator.comparing(Action::getDateTime));

        FULL_PLAYER_ACTIONS.forEach(a -> FULL_PLAYER_ACTIONS_RESPONSE.add(new ActionResponseDTO(a.getDateTime(), a.getMessage())));

        PLAYER_RESPONSE_TO = new PlayerResponseDTO(PLAYER_1_WITH_ID.getId(), PLAYER_1_WITH_ID.getName(), PLAYER_1_WITH_ID.getEmail(), ACCOUNT_1001);
        PLAYER_RESPONSE_TO.setToken(AbstractServiceTest.TOKEN);

        PLAYER_RESPONSE_TO_WITHOUT_TOKEN = new PlayerResponseDTO(PLAYER_1_WITH_ID.getId(), PLAYER_1_WITH_ID.getName(), PLAYER_1_WITH_ID.getEmail(), ACCOUNT_1001);

        MONEY_ACCOUNT_ACTIONS.forEach(a -> MONEY_ACCOUNT_ACTIONS_RESPONSE.add(new ActionResponseDTO(a.getDateTime(), a.getMessage())));
    }
}
