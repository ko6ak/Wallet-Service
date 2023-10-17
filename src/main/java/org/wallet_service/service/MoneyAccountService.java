package org.wallet_service.service;

import org.wallet_service.entity.MoneyAccount;
import org.wallet_service.repository.MoneyAccountRepository;

/**
 * Сервисный класс для Денежного счета Игрока.
 */
public class MoneyAccountService {
    private final MoneyAccountRepository moneyAccountRepository = new MoneyAccountRepository();

    /**
     * Метод сохранения Денежного счета.
     * @param moneyAccount денежный счет.
     * @return Денежный счет.
     */
    public MoneyAccount save(MoneyAccount moneyAccount){
        return moneyAccountRepository.save(moneyAccount);
    }

    public void updateBalance(MoneyAccount moneyAccount){
        moneyAccountRepository.updateBalance(moneyAccount);
    }

    /**
     * Метод получает счет по его id.
     * @param id идентификатор счета.
     * @return Денежный счет.
     */
    public MoneyAccount get(long id){
        return moneyAccountRepository.get(id);
    }
}
