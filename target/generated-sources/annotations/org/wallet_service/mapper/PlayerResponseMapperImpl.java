package org.wallet_service.mapper;

import javax.annotation.processing.Generated;
import org.wallet_service.dto.response.PlayerResponseDTO;
import org.wallet_service.entity.Player;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-30T16:13:57+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
public class PlayerResponseMapperImpl implements PlayerResponseMapper {

    @Override
    public PlayerResponseDTO playerToPlayerResponseTO(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerResponseDTO playerResponseDTO = new PlayerResponseDTO();

        playerResponseDTO.setId( player.getId() );
        playerResponseDTO.setName( player.getName() );
        playerResponseDTO.setEmail( player.getEmail() );
        playerResponseDTO.setMoneyAccount( player.getMoneyAccount() );

        return playerResponseDTO;
    }
}
