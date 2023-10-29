package org.wallet_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wallet_service.dto.response.PlayerResponseDTO;
import org.wallet_service.entity.Player;

/**
 * Интерфейс для преобразования Player в PlayerResponseTO
 */
@Mapper
public interface PlayerResponseMapper {
    PlayerResponseMapper INSTANCE = Mappers.getMapper(PlayerResponseMapper.class);

    PlayerResponseDTO playerToPlayerResponseTO(Player player);
}
