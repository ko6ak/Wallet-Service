package org.wallet_service.service;

import org.springframework.stereotype.Service;
import org.wallet_service.entity.Action;
import org.wallet_service.entity.PlayerAction;
import org.wallet_service.repository.PlayerActionRepository;

import java.util.List;

/**
 * Сервисный класс для событий Игрока.
 */
@Service
public class PlayerActionService {
    private final PlayerActionRepository playerActionRepository;

    public PlayerActionService(PlayerActionRepository playerActionRepository) {
        this.playerActionRepository = playerActionRepository;
    }

    /**
     * Добавление события в список активности для каждого Игрока.
     * @param playerAction событие.
     */
    public void add(PlayerAction playerAction){
        playerActionRepository.add(playerAction);
    }

    /**
     * Получение списка событий для Игрока с указанным идентификатором.
     * @param id идентификатор Игрока.
     * @return Список событий Игрока.
     */
    public List<Action> get(long id){
        return playerActionRepository.get(id);
    }
}
