package org.wallet_service.in;

import org.wallet_service.entity.Player;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.util.Processing;
import org.wallet_service.util.Beans;
import org.wallet_service.dto.PlayerTO;
import org.wallet_service.dto.TransactionTO;
import org.wallet_service.entity.Action;
import org.wallet_service.entity.Operation;
import org.wallet_service.exception.MessageException;
import org.wallet_service.controller.PlayerController;
import org.wallet_service.controller.TransactionController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Класс содержит меню управления приложением и приватные вспомогательные методы.
 */
public class App {
    private static final PlayerController playerController = Beans.getPlayerController();
    private static final TransactionController transactionController = Beans.getTransactionController();

    /**
     * Основной метод приложения.
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        System.out.println("Wallet-Service. Введите номер пункта меню");
        System.out.println("(1) Регистрация Игрока");
        System.out.println("(2) Вход");
        System.out.println("(3) Вывести баланс");
        System.out.println("(4) Вывести лог завершенных транзакций");
        System.out.println("(5) Создать транзакцию");
        System.out.println("(6) Выход");
        System.out.println("(7) Запустить обработку транзакций");
        System.out.println("(8) Вывести лог действий пользователя");
        System.out.println("(9) Выход из программы");
        System.out.print("-> ");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String input;
            Player currentPlayer = null;
            while (!(input = br.readLine()).equals("9")) {
                try {
                    switch (input) {
                        case ("1") -> {
                            PlayerTO playerTO = new PlayerTO();
                            playerTO.setName(checkInput(br, "Имя: "));
                            playerTO.setLogin(checkInput(br, "Логин: "));
                            playerTO.setPassword(checkInput(br, "Пароль: "));
                            System.out.println(playerController.registration(playerTO));
                        }
                        case ("2") -> currentPlayer = playerController.login(checkInput(br, "Логин: "), checkInput(br, "Пароль: "));
                        case ("3") -> System.out.println(playerController.getBalance());
                        case ("4") -> printLog(playerController.getTransactionLog());
                        case ("5") -> {
                            if (currentPlayer == null) throw new AuthenticationException("Сначала нужно залогинится");
                            TransactionTO transactionTO = new TransactionTO();
                            transactionTO.setMoneyAccountId(currentPlayer.getMoneyAccount().getId());
                            transactionTO.setOperation(checkOperation(br, "Выберете тип операции: (1) Пополнение, (2) Списание -> "));
                            transactionTO.setAmount(new BigDecimal(checkNumber(br, "Сумма: ")));
                            transactionTO.setDescription(checkInput(br, "Комментарий: "));
                            transactionTO.setId(UUID.randomUUID());
                            System.out.println(transactionController.register(transactionTO, currentPlayer));
                        }
                        case ("6") -> playerController.logout();
                        case ("7") -> Processing.process();
                        case ("8") -> printLog(playerController.getFullLog(Long.parseLong(checkNumber(br, "Id Игрока: "))));
                    }
                    System.out.print("-> ");
                } catch (MessageException | AuthenticationException | TransactionException e) {
                    System.out.println(e.getMessage());
                    System.out.print("-> ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод будет постоянно требовать ввода правильной строки, полученной из консоли, если она пустая или состоит из пробелов.
     * @param br Объект BufferedReader для получения значения с консоли.
     * @param title Заголовок ввода.
     * @return Проверенная строка.
     */
    private static String checkInput(BufferedReader br, String title) throws IOException {
        String input;
        do {
            System.out.print(title);
            input = br.readLine();
        }
        while (input.isEmpty() || input.isBlank());
        return input;
    }

    /**
     * Метод будет постоянно требовать ввода правильной строки, полученной из консоли, если она пустая, состоит из пробелов и не является 1 или 2.
     * @param br Объект BufferedReader для получения значения с консоли.
     * @param title Заголовок ввода.
     * @return Тип операции.
     */
    private static Operation checkOperation(BufferedReader br, String title) throws IOException {
        String input;
        do {
            System.out.print(title);
            input = br.readLine();
        }
        while (input.isEmpty() || input.isBlank() || !input.equals("1") && !input.equals("2"));
        return input.equals("1") ? Operation.CREDIT : Operation.DEBIT;
    }

    /**
     * Метод будет постоянно требовать ввода правильной строки, полученной из консоли, если она пустая, состоит из пробелов,
     * если строку нельзя интерпретировать как число, в том числе c двумя знаками после точки, если строка является символом 0.00 или если строка является символом 0.
     * @param br Объект BufferedReader для получения значения с консоли.
     * @param title Заголовок ввода.
     * @return Строку, которую можно интерпретировать как число c двумя знаками после точки.
     */
    private static String checkNumber(BufferedReader br, String title) throws IOException {
        String input;
        do {
            System.out.print(title);
            input = br.readLine();
        }
        while (input.isEmpty() || input.isBlank() || !input.matches("\\d+\\.\\d{2}") && !input.matches("\\d+") || input.equals("0.00") || input.equals("0"));
        return input;
    }

    /**
     * Метод проверяет является лог пустым, выводя соответствующее сообщение, или выводит лог в консоль, если он есть.
     * @param actions Лог действий пользователя или лог транзакций.
     */
    private static void printLog(List<Action> actions){
        if (actions.isEmpty()) System.out.println("Лог пустой");
        else actions.forEach(System.out::println);
    }
}
