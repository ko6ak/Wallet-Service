package org.wallet_service.service;

import org.springframework.stereotype.Service;
import org.wallet_service.entity.MoneyAccount;
import org.wallet_service.repository.MoneyAccountRepository;

/**
 * Сервисный класс для Денежного счета Игрока.
 */
@Service
public class MoneyAccountService {
    private final MoneyAccountRepository moneyAccountRepository;

    public MoneyAccountService(MoneyAccountRepository moneyAccountRepository) {
        this.moneyAccountRepository = moneyAccountRepository;
    }

    /**
     * Метод сохранения Денежного счета.
     * @param moneyAccount денежный счет.
     * @return Денежный счет.
     */
    public MoneyAccount save(MoneyAccount moneyAccount){
        return moneyAccountRepository.save(moneyAccount);
    }

    /**
     * Метод для обновления баланса счета.
     * @param moneyAccount счет.
     */
    public boolean updateBalance(MoneyAccount moneyAccount){
        return moneyAccountRepository.updateBalance(moneyAccount);
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
