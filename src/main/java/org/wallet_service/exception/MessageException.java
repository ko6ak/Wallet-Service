package org.wallet_service.exception;

/**
 * Класс содержит сообщение об общей ошибке.
 */
public class MessageException extends RuntimeException{

    /**
     * Метод выводящий сообщение об ошибке в консоль.
     * @param message текст ошибки.
     */
    public MessageException(String message) {
        super(message);
    }
}
