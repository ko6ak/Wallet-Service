package org.wallet_service.entity;

/**
 * Класс содержит данные о типах операций для изменения счета Игрока.
 */
public enum OperationType {
    /**
     * Операция пополнения баланса
     */
    CREDIT,

    /**
     * Операция списания баланса
     */
    DEBIT
}
