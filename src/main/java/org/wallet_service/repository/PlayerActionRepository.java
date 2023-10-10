package org.wallet_service.repository;

import org.wallet_service.entity.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс отвечающий за сохранение событий активности Игрока в хранилище.
 */
public class PlayerActionRepository {
    private final Map<Long, List<Action>> actions = new HashMap<>();

    /**
     * Добавление события в список активности для каждого Игрока.
     * @param id идентификатор Игрока.
     * @param action событие.
     */
    public void add(long id, Action action){
        List<Action> list = actions.get(id);
        if (list == null) list = new ArrayList<>();
        list.add(action);
        actions.put(id, list);
    }

    /**
     * Получение списка событий для Игрока с указанным идентификатором.
     * @param id идентификатор Игрока.
     * @return Список событий Игрока.
     */
    public List<Action> get(long id){
        return actions.get(id);
    }

    /**
     * Удаляет все содержимое из хранилища.
     * Используется только для тестовых классов.
     */
    public void clear(){
        actions.clear();
    }
}
