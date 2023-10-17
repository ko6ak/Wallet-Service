package org.wallet_service.service;

import org.wallet_service.entity.Transaction;
import org.wallet_service.repository.TransactionRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервисный класс для транзакций.
 */
public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();

    /**
     * Проверяет существование Транзакции в хранилище по его id.
     * @param id идентификатор транзакции.
     * @return true/false в зависимости от наличия транзакции в хранилище.
     */
    public boolean isFound(UUID id) {
        return transactionRepository.isFound(id);
    }

    /**
     * Метод возвращает транзакцию по ее UUID.
     * @param id UUID транзакции.
     * @return транзакцию.
     */
    public Transaction get(UUID id){
        return transactionRepository.get(id);
    }

    /**
     * Сохраниение транзакции в хранилище.
     * @param transaction транзакция для сохранения.
     */
    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    /**
     * Метод отмечает транзакцию как обработанную.
     * @param transaction транзакция.
     */
    public void updateProcessed(Transaction transaction){
        transactionRepository.updateProcessed(transaction);
    }

    /**
     * Метод для получения зарегистрированных, но не обработанных транзакций.
     * @return Список необработанных транзакций.
     */
    public List<Transaction> getNotProcessed(){
        return transactionRepository.getNotProcessed();
    }
}
