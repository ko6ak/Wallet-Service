package org.wallet_service.service;

import org.wallet_service.entity.Action;
import org.wallet_service.entity.MoneyAccount;
import org.wallet_service.repository.MoneyAccountActionRepository;
import org.wallet_service.repository.MoneyAccountRepository;

import java.util.List;

/**
 * Сервисный класс отвечающий за сохранение информации о совершенной транзакции для Денежного счета Игрока в хранилище.
 */
public class MoneyAccountActionService {
    private final MoneyAccountActionRepository moneyAccountActionRepository = new MoneyAccountActionRepository();

    /**
     * Добавление информации о совершенной транзакции для Денежного счета.
     * @param action событие.
     */
    public void add(Action action){
        moneyAccountActionRepository.add(action);
    }

    /**
     * Получение совершенных транзакций для Денежного счета с указанным идентификатором.
     * @param money_account_id идентификатор Денежного счета.
     * @return Список транзакций.
     */
    public List<Action> get(long money_account_id){
        return moneyAccountActionRepository.get(money_account_id);
    }
}
