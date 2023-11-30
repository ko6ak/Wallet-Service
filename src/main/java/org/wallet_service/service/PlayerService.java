package org.wallet_service.service;

import org.springframework.stereotype.Service;
import org.wallet_service.entity.Player;
import org.wallet_service.repository.PlayerRepository;

/**
 * Сервисный класс для Игрока.
 */
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Получение Игрока по его логину и паролю.
     * @param email логин Игрока.
     * @param password пароль Игрока.
     * @return Игрока обернутого в Optional.
     */
    public Player get(String email, String password){
        return playerRepository.get(email, password);
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
     * @param email логин Игрока.
     * @return true/false в зависимости от наличия Игрока в хранилище.
     */
    public boolean isFound(String email){
        return playerRepository.isFound(email);
    }
}
