package org.wallet_service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.wallet_service.entity.MoneyAccountAction;
import org.wallet_service.entity.Player;
import org.wallet_service.entity.PlayerAction;
import org.wallet_service.entity.Transaction;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.MoneyAccountActionService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.util.CurrentPlayer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.wallet_service.util.CurrentPlayer.*;

@Aspect
public class LogAspect {
    private static final PlayerActionService playerActionService = new PlayerActionService();
    private static final MoneyAccountActionService moneyAccountActionService = new MoneyAccountActionService();

    @AfterReturning(pointcut = "execution(* org.wallet_service.controller.PlayerController.registration(..))", returning = "player")
    public void loggingRegistration(Player player) {
        playerActionService.add(new PlayerAction(player.getId(), Timestamp.valueOf(LocalDateTime.now()), "Успешная регистрация"));
    }

    @AfterReturning("execution(* org.wallet_service.controller.PlayerController.login(..))")
    public void loggingLogin() {
        playerActionService.add(new PlayerAction(getCurrentPlayer().getId(), Timestamp.valueOf(LocalDateTime.now()), "Успешный вход"));
    }

    @Around("execution(* org.wallet_service.controller.PlayerController.logout(*))")
    public String loggingLogout(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String result;
        try{
            Player currentPlayer = CurrentPlayer.getCurrentPlayer();
            long playerId = 0;
            if (currentPlayer != null) playerId = currentPlayer.getId();
            result = (String) proceedingJoinPoint.proceed();
            playerActionService.add(new PlayerAction(playerId, Timestamp.valueOf(LocalDateTime.now()), "Успешный выход"));
        }
        catch (AuthenticationException e){
            throw new AuthenticationException(e.getMessage());
        }
        return result;
    }

    @AfterReturning("execution(* org.wallet_service.controller.PlayerController.getBalance(*))")
    public void loggingBalance() {
        playerActionService.add(new PlayerAction(getCurrentPlayer().getId(), Timestamp.valueOf(LocalDateTime.now()), "Вывод информации о балансе"));
    }

    @AfterReturning("execution(* org.wallet_service.controller.PlayerController.getTransactionLog(*))")
    public void loggingTransactionLog() {
        playerActionService.add(new PlayerAction(getCurrentPlayer().getId(), Timestamp.valueOf(LocalDateTime.now()), "Вывод лога транзакций"));
    }

    @AfterReturning(pointcut = "execution(* org.wallet_service.controller.TransactionController.register(..))")
    public void loggingTransactionLog(JoinPoint joinPoint) {
        Object[] input = joinPoint.getArgs();
        playerActionService.add(new PlayerAction(getCurrentPlayer().getId(), Timestamp.valueOf(LocalDateTime.now()),
                "Создана транзакция с типом операции " + input[1] +
                        ", суммой " + input[2] + " и комментарием '" + input[3] + "'"));
    }

    @Around("execution(* org.wallet_service.util.Processing.debit(*))")
    public void loggingDebitException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Transaction transaction = (Transaction) proceedingJoinPoint.getArgs()[0];
        try{
            proceedingJoinPoint.proceed();
            moneyAccountActionService.add(new MoneyAccountAction(transaction.getMoneyAccountId(), Timestamp.valueOf(LocalDateTime.now()),
                    "Транзакция с типом операции " + transaction.getOperation() +
                            ", суммой " + transaction.getAmount() + " и комментарием '" + transaction.getDescription() + "' успешно выполнена"));
        }
        catch (TransactionException e) {
            moneyAccountActionService.add(new MoneyAccountAction(transaction.getMoneyAccountId(), Timestamp.valueOf(LocalDateTime.now()),
                    "Транзакция с типом операции " + transaction.getOperation() +
                            ", суммой " + transaction.getAmount() + " и комментарием '" + transaction.getDescription() +
                            "' не выполнена. Причина: " + e.getMessage()));
        }
    }

    @Around("execution(* org.wallet_service.util.Processing.credit(*))")
    public void loggingCreditException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Transaction transaction = (Transaction) proceedingJoinPoint.getArgs()[0];
        proceedingJoinPoint.proceed();
        moneyAccountActionService.add(new MoneyAccountAction(transaction.getMoneyAccountId(), Timestamp.valueOf(LocalDateTime.now()),
                "Транзакция с типом операции " + transaction.getOperation() +
                        ", суммой " + transaction.getAmount() + " и комментарием '" + transaction.getDescription() + "' успешно выполнена"));
    }
}
