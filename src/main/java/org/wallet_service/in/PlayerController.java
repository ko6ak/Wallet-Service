package org.wallet_service.in;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wallet_service.aspect.Time;
import org.wallet_service.dto.request.LoginRequestDTO;
import org.wallet_service.dto.request.PlayerRequestDTO;
import org.wallet_service.dto.request.TokenRequestDTO;
import org.wallet_service.dto.response.ActionResponseDTO;
import org.wallet_service.dto.response.MessageResponseDTO;
import org.wallet_service.dto.response.PlayerResponseDTO;
import org.wallet_service.entity.*;
import org.wallet_service.exception.AuthenticationException;
import org.wallet_service.mapper.ActionResponseMapper;
import org.wallet_service.mapper.PlayerResponseMapper;
import org.wallet_service.service.MoneyAccountActionService;
import org.wallet_service.service.MoneyAccountService;
import org.wallet_service.service.PlayerActionService;
import org.wallet_service.service.PlayerService;
import org.wallet_service.util.JWT;
import org.wallet_service.validator.Validator;

import java.math.BigDecimal;
import java.util.*;

import static org.wallet_service.util.CurrentPlayer.*;

/**
 * Класс отвечает за обслуживание объекта Player.
 */
@Time
@RestController
public class PlayerController {
    public static final String EMAIL_IS_ALREADY_EXIST = "Игрок с таким email уже есть в системе";
    public static final String PLAYER_WITH_THIS_EMAIL_NOT_FOUND = "Игрок с таким email не найден";
    public static final String WRONG_PASSWORD = "Неправильный пароль";
    public static final String EXIT_FIRST = "Сначала нужно выйти";
    public static final String NOT_LOGGED_IN = "Вы не залогинены";
    public static final String EXPIRED_TOKEN = "Токен просрочен, залогинтесь заново";
    public static final String PLAYER_WITH_THIS_ID_NOT_FOUND = "Игрок с таким id не найден";
    public static final String GOODBYE = "Пока!";

    private final PlayerService playerService;
    private final MoneyAccountService moneyAccountService;
    private final PlayerActionService playerActionService;
    private final MoneyAccountActionService moneyAccountActionService;
    private final Validator validator;

    public PlayerController(PlayerService playerService,
                            MoneyAccountService moneyAccountService,
                            PlayerActionService playerActionService,
                            MoneyAccountActionService moneyAccountActionService,
                            Validator validator) {
        this.playerService = playerService;
        this.moneyAccountService = moneyAccountService;
        this.playerActionService = playerActionService;
        this.moneyAccountActionService = moneyAccountActionService;
        this.validator = validator;
    }

    /**
     * Регистрация Игрока.
     * Метод регистрирует Игрока в системе и создает для него Денежный счет (MoneyAccount).
     * @param playerRequestDTO входные данные для регистрации.
     * @return игрока, сообщение о существующем email или сообщение о проблемах во входящих данных.
     */
    @PostMapping("registration")
    public ResponseEntity<?> registration(@RequestBody PlayerRequestDTO playerRequestDTO) {
        String name = playerRequestDTO.getName();
        String email = playerRequestDTO.getEmail();
        String password = playerRequestDTO.getPassword();

        Map<String, String> notBlank = new HashMap<>();
        notBlank.put("name", name);
        notBlank.put("email", email);
        notBlank.put("password", password);

        Map<String, String> sizePassword = new HashMap<>();
        sizePassword.put("password", password);

        Map<String, String> size = new HashMap<>();
        size.put("name", name);

        Map<String, String> checkEmail = new HashMap<>();
        checkEmail.put("email", email);

        validator.clearResult();
        validator.checkNotBlank(notBlank);
        validator.checkSizePassword(sizePassword);
        validator.checkSize(size);
        validator.checkEmail(checkEmail);

        if (!validator.getResult().isEmpty())
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(validator.getResult());

        if (!playerService.isFound(email)) {
            MoneyAccount moneyAccount = moneyAccountService.save(new MoneyAccount(new BigDecimal("0.00")));
            Player player = playerService.save(new Player(name, email, password, moneyAccount));
            return ResponseEntity.ok(PlayerResponseMapper.INSTANCE.playerToPlayerResponseTO(player));
        }
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO(EMAIL_IS_ALREADY_EXIST));
    }

    /**
     * Метод позволяет Игроку залогинится в системе. Создает токен аутентификации.
     * @param loginRequestDTO логин и пароль Игрока.
     * @return Игрока или сообщение, если Игрок с таким email уже вошел, если такого email нет в системе или введен неправильный пароль.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){
        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();

        Map<String, String> notBlank = new HashMap<>();
        notBlank.put("email", email);
        notBlank.put("password", password);

        validator.clearResult();
        validator.checkNotBlank(notBlank);

        if (!validator.getResult().isEmpty())
            return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(validator.getResult());

        if (getCurrentPlayer() == null) {
            if (!playerService.isFound(email))
                return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(new MessageResponseDTO(PLAYER_WITH_THIS_EMAIL_NOT_FOUND));

            Player player = playerService.get(email, password);

            if (player == null)
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO(WRONG_PASSWORD));

            String token = JWT.create(player);
            setToken(token);
            setCurrentPlayer(player);

            PlayerResponseDTO playerResponseDTO = PlayerResponseMapper.INSTANCE.playerToPlayerResponseTO(player);
            playerResponseDTO.setToken(token);

            return ResponseEntity.ok(playerResponseDTO);
        }
        else return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO(EXIT_FIRST));
    }

    /**
     * Метод выхода из системы.
     * @param tokenRequestDTO токен вошедшего Игрока.
     * @return сообщение об успешном выходе или если Игрок не залогинен.
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDTO> logout(@RequestBody TokenRequestDTO tokenRequestDTO){
        if (getCurrentPlayer() != null && tokenRequestDTO.getToken().equals(getToken())) {
            setCurrentPlayer(null);
            setToken(null);
            return ResponseEntity.ok(new MessageResponseDTO(GOODBYE));
        }
        else return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO(NOT_LOGGED_IN));
    }

    /**
     * Метод возвращает баланс залогиненного игрока.
     * @param tokenRequestDTO токен вошедшего Игрока.
     * @return баланс залогиненного игрока или сообщение если Игрок не залогинен, токен из параметра метода не совпадает с сохраненным в системе или токен просрочен.
     */
    @PostMapping("/balance")
    public ResponseEntity<MessageResponseDTO> getBalance(@RequestBody TokenRequestDTO tokenRequestDTO){

        String token = tokenRequestDTO.getToken();
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer != null && token.equals(getToken())) {
            try{
                JWT.validate(token);
                return ResponseEntity.ok(new MessageResponseDTO(playerService.get(currentPlayer.getId()).getMoneyAccount().getBalance().toString()));
            }
            catch (AuthenticationException e){
                setCurrentPlayer(null);
                setToken(null);
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO(EXPIRED_TOKEN));
            }
        }
        else return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO(NOT_LOGGED_IN));
    }

    /**
     * Метод позволяет получить лог транзакций залогиненного Игрока.
     * @param tokenRequestDTO токен вошедшего Игрока.
     * @return Лог транзакций или сообщение если Игрок не залогинен, токен из параметра метода не совпадает с сохраненным в системе или токен просрочен.
     */
    @PostMapping("/transaction-log")
    public ResponseEntity<?> getTransactionLog(@RequestBody TokenRequestDTO tokenRequestDTO){

        String token = tokenRequestDTO.getToken();
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer != null && token.equals(getToken())) {
            try{
                JWT.validate(token);

                List<Action> actions = moneyAccountActionService.get(currentPlayer.getMoneyAccount().getId());
                List<ActionResponseDTO> actionResponseDTOs = new ArrayList<>();
                actions.forEach(a -> actionResponseDTOs.add(ActionResponseMapper.INSTANCE.ActionToActionResponseTO(a)));

                return ResponseEntity.ok(actionResponseDTOs);
            }
            catch (AuthenticationException e){
                setCurrentPlayer(null);
                setToken(null);
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO(EXPIRED_TOKEN));
            }
        }
        else return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO(NOT_LOGGED_IN));
    }

    /**
     * Метод позволяет получить полный лог действий Игрока, включая транзакции, по его id, вход Игрока в систему не требуется.
     * @param playerId идентификатор Игрока.
     * @return Лог действий Игрока, включая транзакции или сообщение если Игрок с таким id не найден.
     */
    @GetMapping("/full-log")
    public ResponseEntity<?> getFullLog(@RequestParam(name = "id") long playerId){

        Player player = playerService.get(playerId);

        if (player == null)
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(new MessageResponseDTO(PLAYER_WITH_THIS_ID_NOT_FOUND));

        List<Action> actions = moneyAccountActionService.get(player.getMoneyAccount().getId());
        actions.addAll(playerActionService.get(playerId));
        actions.sort(Comparator.comparing(Action::getDateTime));

        List<ActionResponseDTO> actionResponseDTOs = new ArrayList<>();
        actions.forEach(a -> actionResponseDTOs.add(ActionResponseMapper.INSTANCE.ActionToActionResponseTO(a)));

        return ResponseEntity.ok(actionResponseDTOs);
    }
}
