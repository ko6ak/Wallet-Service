package org.wallet_service.in;

import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wallet_service.aspect.Time;
import org.wallet_service.dto.request.TransactionRequestDTO;
import org.wallet_service.dto.response.MessageResponseDTO;
import org.wallet_service.entity.OperationType;
import org.wallet_service.entity.Player;
import org.wallet_service.entity.Transaction;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.exception.TransactionException;
import org.wallet_service.service.TransactionService;
import org.wallet_service.util.CurrentPlayer;
import org.wallet_service.util.JWT;
import org.wallet_service.util.Processing;
import org.wallet_service.validator.Validator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.wallet_service.util.CurrentPlayer.*;

/**
 * Класс отвечает за обслуживание Транзакций.
 */
@Time
@RestController
@Component
public class TransactionController {
    public static final String REGISTER_SUCCESS = "Зарегистрировано";

    private final TransactionService transactionService;
    private final Validator validator;
    private final Processing processing;

    public TransactionController(TransactionService transactionService, Validator validator, Processing processing) {
        this.transactionService = transactionService;
        this.validator = validator;
        this.processing = processing;
    }

    /**
     * Метод регистрирует транзакции в системе.
//     * @param id идентификатор транзакции.
//     * @param operation тип операции.
//     * @param amount сумма.
//     * @param description комментарий.
//     * @param token токен.
     * @return Сообщение об успешной регистрации.
     * @throws TransactionException если не уникальный id транзакции.
     * @throws AuthenticationException если Игрок не залогинен, токен из параметра метода не совпадает с сохраненным в системе или токен просрочен.
     */
    @PostMapping("/transaction-registration")
    public ResponseEntity<?> register(@RequestBody TransactionRequestDTO transactionRequestDTO){

        String inputId = transactionRequestDTO.getId();
        String inputOperation = transactionRequestDTO.getOperation();
        String amount = transactionRequestDTO.getAmount();
        String description = transactionRequestDTO.getDescription();
        String token = transactionRequestDTO.getToken();

        Map<String, String> notBlank = new HashMap<>();
        notBlank.put("id", inputId);
        notBlank.put("operation", inputOperation);
        notBlank.put("description", description);
        notBlank.put("token", token);

        Map<String, String> checkAmount = new HashMap<>();
        checkAmount.put("amount", amount);

        validator.clearResult();
        validator.checkNotBlank(notBlank);
        validator.checkFormat(checkAmount);
        validator.checkMinDecimal(checkAmount);

        if (!validator.getResult().isEmpty())
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(validator.getResult());

        UUID id = UUID.fromString(inputId);
        OperationType operationType = OperationType.valueOf(inputOperation);

        Player currentPlayer = CurrentPlayer.getCurrentPlayer();
        if (currentPlayer == null || !token.equals(getToken()))
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Сначала нужно залогинится"));
        try{
            JWT.validate(token);
        }
        catch (AuthenticationException e){
            setCurrentPlayer(null);
            setToken(null);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Токен просрочен, залогинтесь заново"));
        }
        if (!transactionService.isFound(id)) {
            transactionService.save(new Transaction(id, LocalDateTime.now(), description, operationType,
                    new BigDecimal(amount), currentPlayer.getMoneyAccount().getId(), false));
        }
        else return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(new MessageResponseDTO("Не уникальный id транзакции"));
        return ResponseEntity.ok(new MessageResponseDTO(REGISTER_SUCCESS));
    }

    /**
     * Метод получает не обработанные транзакции и обрабатывает их в соответствии с типом операции.
     * Выводит сообщение о завершении обработки или если транзакций для обработки нет. Не прерывает работу если транзакцию выполнить не удалось.
     */
    @GetMapping("/process")
    public ResponseEntity<?> process(){
        List<Transaction> transactions = transactionService.getNotProcessed();
        if (!transactions.isEmpty()) {
            transactions.forEach(t -> {
                switch (t.getOperation()) {
                    case CREDIT -> processing.credit(t);
                    case DEBIT -> processing.debit(t);
                }
                transactionService.updateProcessed(t);
            });
            return ResponseEntity.ok(new MessageResponseDTO("Все транзакции обработаны"));
        }
        else return ResponseEntity.ok(new MessageResponseDTO("Нет транзакций для обработки"));
    }
}
