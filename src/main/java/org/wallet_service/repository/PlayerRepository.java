package org.wallet_service.repository;

import org.wallet_service.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Класс отвечающий за сохранение Игрока в хранилище.
 */
public class PlayerRepository {
    private static long id = 0;
    private final Map<Long, Player> players = new HashMap<>();

    /**
     * Получение Игрока по его логину и паролю.
     * @param login логин Игрока.
     * @param password пароль Игрока.
     * @return Игрока обернутого в Optional.
     */
    public Optional<Player> get(String login, String password){
        return players.values().stream().filter(p -> p.getLogin().equals(login) && p.getPassword().equals(password)).findFirst();
    }

    /**
     * Получение Игрока по id.
     * @param id идентификатор Игрока
     * @return Игрока или null если Игрок не найден.
     */
    public Player get(long id){
        return players.get(id);
    }

    /**
     * Метод генерирует идентификатор для Игрока.
     * @return id Игрока
     */
    private static long getId(){
        return ++id;
    }

    /**
     * Сохраняет Игрока в хранилище.
     * @param player Игрок для сохранения.
     * @return Игрока.
     */
    public Player save(Player player){
        long id = getId();
        player.setId(id);
        players.put(id, player);
        return player;
    }

    /**
     * Проверяет существование Игрока в хранилище по его логину.
     * @param login логин Игрока.
     * @return true/false в зависимости от наличия Игрока в хранилище.
     */
    public boolean isFound(String login){
        Optional<Player> player = players.values().stream().filter(p -> p.getLogin().equals(login)).findFirst();
        return player.isPresent();
    }

    /**
     * Удаляет все содержимое из хранилища и возвращает генерируемое значение к начальному значению.
     * Используется только для тестовых классов.
     */
    public void clear(){
        players.clear();
        id = 0;
    }
}
