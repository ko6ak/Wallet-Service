package org.wallet_service.repository;

import org.wallet_service.entity.Transaction;

import java.util.*;

/**
 * Класс отвечающий за сохранение Транзакций в хранилище.
 */
public class TransactionRepository {
    private final Map<UUID, Transaction> transactions = new LinkedHashMap<>();

    /**
     * Проверяет существование Транзакции в хранилище по его id.
     * @param id идентификатор транзакции.
     * @return true/false в зависимости от наличия транзакции в хранилище.
     */
    public boolean isFound(UUID id){
        return transactions.containsKey(id);
    }

    /**
     * Метод возвращает транзакцию по ее UUID.
     * @param id UUID транзакции.
     * @return транзакцию.
     */
    public Transaction get(UUID id){
        return transactions.get(id);
    }

    /**
     * Сохраниение транзакции ы хранилище.
     * @param transaction транзакция для сохранения.
     */
    public Transaction save(Transaction transaction){
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    /**
     * Метод для получения зарегистрированных, но не обработанных транзакций.
     * @return Список необработанных транзакций.
     */
    public List<Transaction> getNotProcessed(){
        List<Transaction> notProcessedTransactions = new ArrayList<>();
        transactions.forEach((id, t) -> {
            if (!t.isProcessed()) notProcessedTransactions.add(t);
        });
        notProcessedTransactions.sort(Comparator.comparing(Transaction::getDateTime));
        return notProcessedTransactions;
    }

    /**
     * Удаляет все содержимое из хранилища.
     * Используется только для тестовых классов.
     */
    public void clear(){
        transactions.clear();
    }
}
