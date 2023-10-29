package org.wallet_service.in;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
import org.wallet_service.exception.MessageException;
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
@Component
//@Api("туц-туц-туц!")
public class PlayerController {
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
     * @param name имя Игрока.
     * @param email Почта Игрока.
     * @param password пароль Игрока.
     * @return игрока.
     * @throws AuthenticationException если Игрок с таким email уже есть в системе.
     */
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "uuid", value = "User's uuid", required = true, dataType = "string", paramType = "query")
//    })
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
            return ResponseEntity.ok(playerService.save(new Player(name, email, password, moneyAccount)));
        }
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Игрок с таким email уже есть в системе"));
    }

    /**
     * Метод позволяет Игроку залогинится в системе. Создает токен аутентификации.
//     * @param email логин Игрока.
//     * @param password пароль Игрока.
     * @return объект Игрока.
     * @throws AuthenticationException если Игрок с таким email уже вошел, если такого email нет в системе или введен неправильный пароль.
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
                return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(new MessageResponseDTO("Игрок с таким email не найден"));

            Player player = playerService.get(email, password);

            if (player == null)
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Неправильный пароль"));

            String token = JWT.create(player);
            setToken(token);
            setCurrentPlayer(player);

            PlayerResponseDTO playerResponseDTO = PlayerResponseMapper.INSTANCE.playerToPlayerResponseTO(player);
            playerResponseDTO.setToken(token);

            return ResponseEntity.ok(playerResponseDTO);
        }
        else return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Сначала нужно выйти"));
    }

    /**
     * Метод выхода из системы.
     * @param token токен вошедшего Игрока.
     * @throws AuthenticationException если Игрок не залогинен.
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDTO> logout(@RequestBody TokenRequestDTO tokenRequestDTO){
        if (getCurrentPlayer() != null && tokenRequestDTO.getToken().equals(getToken())) {
            setCurrentPlayer(null);
            setToken(null);
            return ResponseEntity.ok(new MessageResponseDTO("Пока!"));
        }
        else return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Вы не залогинены"));
    }

    /**
     * Метод возвращает баланс залогиненного игрока.
     * @param token токен вошедшего Игрока.
     * @return баланс залогиненного игрока.
     * @throws AuthenticationException если Игрок не залогинен, токен из параметра метода не совпадает с сохраненным в системе или токен просрочен.
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
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Токен просрочен, залогинтесь заново"));
            }
        }
        else return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Вы не залогинены"));
    }

    /**
     * Метод позволяет получить лог транзакций залогиненного Игрока.
     * @param token токен вошедшего Игрока.
     * @return Лог транзакций.
     * @throws AuthenticationException если Игрок не залогинен, токен из параметра метода не совпадает с сохраненным в системе или токен просрочен.
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
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Токен просрочен, залогинтесь заново"));
            }
        }
        else return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(new MessageResponseDTO("Вы не залогинены"));
    }

    /**
     * Метод позволяет получить полный лог действий Игрока, включая транзакции, по его id, вход Игрока в систему не требуется.
     * @param playerId идентификатор Игрока.
     * @return Лог действий Игрока, включая транзакции.
     * @throws MessageException если Игрок с таким id не найден.
     */
    @GetMapping("/full-log")
    public ResponseEntity<?> getFullLog(@RequestParam(name = "id") long playerId){

        Player player = playerService.get(playerId);

        if (player == null)
            return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(new MessageResponseDTO("Игрок с таким id не найден"));

        List<Action> actions = moneyAccountActionService.get(player.getMoneyAccount().getId());
        actions.addAll(playerActionService.get(playerId));
        actions.sort(Comparator.comparing(Action::getDateTime));

        List<ActionResponseDTO> actionResponseDTOs = new ArrayList<>();
        actions.forEach(a -> actionResponseDTOs.add(ActionResponseMapper.INSTANCE.ActionToActionResponseTO(a)));

        return ResponseEntity.ok(actionResponseDTOs);
    }
}
