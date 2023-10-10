package org.wallet_service.repository;

import org.wallet_service.entity.MoneyAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Класс отвечающий за сохранение Денежного счета Игрока в хранилище.
 */
public class MoneyAccountRepository {
    private static long id = 1000;
    private final Map<Long, MoneyAccount> moneyAccounts = new HashMap<>();

    /**
     * Метод сохранения Денежного счета.
     * @param moneyAccount денежный счет.
     * @return Денежный счет.
     */
    public MoneyAccount save(MoneyAccount moneyAccount){
        long id = getId();
        moneyAccount.setId(id);
        moneyAccounts.put(id, moneyAccount);
        return moneyAccount;
    }

    /**
     * Метод генерирует идентификатор для счета. Начальное значение для идентификатора - 1000.
     * @return id счета
     */
    private static long getId(){
        return ++id;
    }

    /**
     * Метод получает счет по его id.
     * @param id идентификатор счета.
     * @return Денежный счет.
     */
    public MoneyAccount get(long id){
        return moneyAccounts.get(id);
    }

    /**
     * Удаляет все содержимое из хранилища и возвращает генерируемое значение к начальному значению.
     * Используется только для тестовых классов.
     */
    public void clear(){
        moneyAccounts.clear();
        id = 1000;
    }
}
