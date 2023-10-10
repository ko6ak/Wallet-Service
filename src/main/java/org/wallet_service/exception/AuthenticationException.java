package org.wallet_service.exception;

/**
 * Класс содержит сообщение об ошибке аутентификации Игрока.
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Метод выводящий сообщение об ошибке в консоль.
     * @param message текст ошибки.
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
