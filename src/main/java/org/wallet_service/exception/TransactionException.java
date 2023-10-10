package org.wallet_service.exception;

/**
 * Класс содержит сообщение об ошибке транзакции.
 */
public class TransactionException extends RuntimeException{

    /**
     * Метод выводящий сообщение об ошибке в консоль.
     * @param message текст ошибки.
     */
    public TransactionException(String message) {
        super(message);
    }
}
