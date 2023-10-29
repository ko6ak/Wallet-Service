package org.wallet_service.service;

import org.springframework.stereotype.Service;
import org.wallet_service.entity.Action;
import org.wallet_service.entity.MoneyAccountAction;
import org.wallet_service.repository.MoneyAccountActionRepository;

import java.util.List;

/**
 * Сервисный класс отвечающий за сохранение информации о совершенной транзакции для Денежного счета Игрока в хранилище.
 */
@Service
public class MoneyAccountActionService {
    private final MoneyAccountActionRepository moneyAccountActionRepository;

    public MoneyAccountActionService(MoneyAccountActionRepository moneyAccountActionRepository) {
        this.moneyAccountActionRepository = moneyAccountActionRepository;
    }

    /**
     * Добавление информации о совершенной транзакции для Денежного счета.
     * @param moneyAccountAction событие.
     */
    public void add(MoneyAccountAction moneyAccountAction){
        moneyAccountActionRepository.add(moneyAccountAction);
    }

    /**
     * Получение совершенных транзакций для Денежного счета с указанным идентификатором.
     * @param moneyAccountId идентификатор Денежного счета.
     * @return Список транзакций.
     */
    public List<Action> get(long moneyAccountId){
        return moneyAccountActionRepository.get(moneyAccountId);
    }
}
