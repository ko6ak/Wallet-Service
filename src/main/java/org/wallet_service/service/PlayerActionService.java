package org.wallet_service.service;

import org.wallet_service.entity.Action;
import org.wallet_service.repository.PlayerActionRepository;

import java.util.List;

/**
 * Сервисный класс для событий Игрока.
 */
public class PlayerActionService {
    private final PlayerActionRepository playerActionRepository = new PlayerActionRepository();

    /**
     * Добавление события в список активности для каждого Игрока.
     * @param id идентификатор Игрока.
     * @param action событие.
     */
    public void add(long id, Action action){
        playerActionRepository.add(id, action);
    }

    /**
     * Получение списка событий для Игрока с указанным идентификатором.
     * @param id идентификатор Игрока.
     * @return Список событий Игрока.
     */
    public List<Action> get(long id){
        return playerActionRepository.get(id);
    }

    /**
     * Удаляет все содержимое из хранилища.
     * Используется только для тестовых классов.
     */
    public void clear(){
        playerActionRepository.clear();
    }
}
