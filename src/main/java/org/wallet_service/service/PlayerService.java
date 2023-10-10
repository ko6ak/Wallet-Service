package org.wallet_service.service;

import org.wallet_service.entity.Player;
import org.wallet_service.repository.PlayerRepository;

import java.util.Optional;

/**
 * Сервисный класс для Игрока.
 */
public class PlayerService {
    private final PlayerRepository playerRepository = new PlayerRepository();

    /**
     * Получение Игрока по его логину и паролю.
     * @param login логин Игрока.
     * @param password пароль Игрока.
     * @return Игрока обернутого в Optional.
     */
    public Optional<Player> get(String login, String password){
        return playerRepository.get(login, password);
    }

    /**
     * Получение Игрока по id.
     * @param id идентификатор Игрока
     * @return Игрока или null если Игрок не найден.
     */
    public Player get(long id){
        return playerRepository.get(id);
    }

    /**
     * Сохраняет Игрока в хранилище.
     * @param player Игрок для сохранения.
     * @return Игрока.
     */
    public Player save(Player player){
        return playerRepository.save(player);
    }

    /**
     * Проверяет существование Игрока в хранилище по его логину.
     * @param login логин Игрока.
     * @return true/false в зависимости от наличия Игрока в хранилище.
     */
    public boolean isFound(String login){
        return playerRepository.isFound(login);
    }

    /**
     * Удаляет все содержимое из хранилища и возвращает генерируемое значение к начальному значению.
     * Используется только для тестовых классов.
     */
    public void clear(){
        playerRepository.clear();
    }
}
