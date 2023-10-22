package org.wallet_service.mapper;

import javax.annotation.processing.Generated;
import org.wallet_service.dto.response.PlayerResponseTO;
import org.wallet_service.entity.MoneyAccount;
import org.wallet_service.entity.Player;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-22T23:23:13+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
public class PlayerResponseMapperImpl implements PlayerResponseMapper {

    @Override
    public PlayerResponseTO playerToPlayerResponseTO(Player player) {
        if ( player == null ) {
            return null;
        }

        long id = 0L;
        String name = null;
        String email = null;
        MoneyAccount moneyAccount = null;

        id = player.getId();
        name = player.getName();
        email = player.getEmail();
        moneyAccount = player.getMoneyAccount();

        PlayerResponseTO playerResponseTO = new PlayerResponseTO( id, name, email, moneyAccount );

        return playerResponseTO;
    }
}
