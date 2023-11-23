package org.wallet_service.aspect;

import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.wallet_service.dto.request.TransactionRequestDTO;
import org.wallet_service.dto.response.MessageResponseDTO;
import org.wallet_service.entity.*;
import org.wallet_service.service.MoneyAccountActionService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.util.CurrentPlayer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.wallet_service.util.CurrentPlayer.*;

/**
 * Класс-аспект логирующий действия Игрока.
 */
@Aspect
@Component
public class LogAspect {
    private final PlayerActionService playerActionService;
    private final MoneyAccountActionService moneyAccountActionService;

    public LogAspect(PlayerActionService playerActionService, MoneyAccountActionService moneyAccountActionService) {
        this.playerActionService = playerActionService;
        this.moneyAccountActionService = moneyAccountActionService;
    }

    /**
     * Метод логирует успешную регистрацию Игрока.
     */
    @AfterReturning(pointcut = "execution(* org.wallet_service.in.PlayerController.registration(*))", returning = "response")
    public void loggingRegistration(ResponseEntity<?> response) {
        if (response.getStatusCode().value() == HttpServletResponse.SC_OK) {
            Player player = (Player) response.getBody();
            if (player != null)
                playerActionService.add(new PlayerAction(player.getId(), Timestamp.valueOf(LocalDateTime.now()), "Успешная регистрация"));
        }
    }

    /**
     * Метод логирует успешный вход Игрока в приложение.
     */
    @AfterReturning("execution(* org.wallet_service.in.PlayerController.login(*))")
    public void loggingLogin() {
        Player player = getCurrentPlayer();
        if (player != null)
            playerActionService.add(new PlayerAction(player.getId(), Timestamp.valueOf(LocalDateTime.now()), "Успешный вход"));
    }

    /**
     * Метод логирует успешный выход Игрока из приложения.
     */
    @Around("execution(* org.wallet_service.in.PlayerController.logout(*))")
    public ResponseEntity<MessageResponseDTO> loggingLogout(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Player currentPlayer = CurrentPlayer.getCurrentPlayer();
        long playerId = 0;
        if (currentPlayer != null) playerId = currentPlayer.getId();

        ResponseEntity<MessageResponseDTO> response = (ResponseEntity<MessageResponseDTO>) proceedingJoinPoint.proceed();

        if (response.getStatusCode().value() == HttpServletResponse.SC_OK)
            playerActionService.add(new PlayerAction(playerId, Timestamp.valueOf(LocalDateTime.now()), "Успешный выход"));

        return response;
    }

    /**
     * Метод логирует запрос баланса Игрока.
     */
    @AfterReturning(pointcut = "execution(* org.wallet_service.in.PlayerController.getBalance(*))", returning = "response")
    public void loggingBalance(ResponseEntity<MessageResponseDTO> response) {
        if (response.getStatusCode().value() == HttpServletResponse.SC_OK)
            playerActionService.add(new PlayerAction(getCurrentPlayer().getId(), Timestamp.valueOf(LocalDateTime.now()), "Вывод информации о балансе"));
    }

    /**
     * Метод логирует запрос лога транзакций Игрока.
     */
    @AfterReturning(pointcut = "execution(* org.wallet_service.in.PlayerController.getTransactionLog(*))", returning = "response")
    public void loggingTransactionLog(ResponseEntity<?> response) {
        if (response.getStatusCode().value() == HttpServletResponse.SC_OK)
            playerActionService.add(new PlayerAction(getCurrentPlayer().getId(), Timestamp.valueOf(LocalDateTime.now()), "Вывод лога транзакций"));
    }

    /**
     * Метод логирует создание транзакции Игроком.
     */
    @AfterReturning(pointcut = "execution(* org.wallet_service.in.TransactionController.register(*))", returning = "response")
    public void loggingTransactionLog(JoinPoint joinPoint, ResponseEntity<?> response) {
        if (response.getStatusCode().value() == HttpServletResponse.SC_OK) {
            TransactionRequestDTO transactionRequestDTO = (TransactionRequestDTO) joinPoint.getArgs()[0];
            String operation = transactionRequestDTO.getOperation();
            String amount = transactionRequestDTO.getAmount();
            String description = transactionRequestDTO.getDescription();

            playerActionService.add(new PlayerAction(getCurrentPlayer().getId(), Timestamp.valueOf(LocalDateTime.now()),
                    "Создана транзакция с типом операции " + operation +
                            ", суммой " + amount + " и комментарием '" + description + "'"));
        }
    }

    /**
     * Метод логирует результат обработки транзацкии.
     */
    @AfterReturning(pointcut = "execution(* org.wallet_service.util.Processing.debit(*))", returning = "bool")
    public void loggingDebit(JoinPoint joinPoint, boolean bool) throws Throwable {
        Transaction transaction = (Transaction) joinPoint.getArgs()[0];

        String message = "Транзакция с типом операции " + transaction.getOperation() +
                ", суммой " + transaction.getAmount() + " и комментарием '" + transaction.getDescription();

        if (bool) moneyAccountActionService.add(
                new MoneyAccountAction(
                        transaction.getMoneyAccountId(),
                        Timestamp.valueOf(LocalDateTime.now()),
                        message + "' успешно выполнена"));
        else moneyAccountActionService.add(
                new MoneyAccountAction(
                        transaction.getMoneyAccountId(),
                        Timestamp.valueOf(LocalDateTime.now()),
                        message + "' не выполнена: баланс меньше списываемой суммы"));
    }

    /**
     * Метод логирует результат обработки транзацкии.
     */
    @AfterReturning(pointcut = "execution(* org.wallet_service.util.Processing.credit(*))", returning = "bool")
    public void loggingCredit(JoinPoint joinPoint, boolean bool) throws Throwable {
        Transaction transaction = (Transaction) joinPoint.getArgs()[0];

        String message = "Транзакция с типом операции " + transaction.getOperation() +
                ", суммой " + transaction.getAmount() + " и комментарием '" + transaction.getDescription();

        System.out.println(bool);
        System.out.println("bool");

        if (bool) moneyAccountActionService.add(
                new MoneyAccountAction(
                        transaction.getMoneyAccountId(),
                        Timestamp.valueOf(LocalDateTime.now()),
                        message + "' успешно выполнена"));
    }
}
