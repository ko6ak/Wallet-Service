package org.wallet_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wallet_service.dto.response.PlayerResponseTO;
import org.wallet_service.entity.Player;

@Mapper
public interface PlayerResponseMapper {
    PlayerResponseMapper INSTANCE = Mappers.getMapper(PlayerResponseMapper.class);

    PlayerResponseTO playerToPlayerResponseTO(Player player);
}
