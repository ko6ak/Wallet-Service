package org.wallet_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wallet_service.dto.response.ActionResponseTO;
import org.wallet_service.entity.Action;

@Mapper
public interface ActionResponseMapper {
    ActionResponseMapper INSTANCE = Mappers.getMapper(ActionResponseMapper.class);

    ActionResponseTO ActionToActionResponseTO(Action action);
}
