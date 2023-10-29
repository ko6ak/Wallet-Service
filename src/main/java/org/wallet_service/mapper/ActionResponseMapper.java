package org.wallet_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wallet_service.dto.response.ActionResponseDTO;
import org.wallet_service.entity.Action;

/**
 * Интерфейс для преобразования Action в ActionResponseTO
 */
@Mapper
public interface ActionResponseMapper {
    ActionResponseMapper INSTANCE = Mappers.getMapper(ActionResponseMapper.class);

    ActionResponseDTO ActionToActionResponseTO(Action action);
}
