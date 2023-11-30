package org.wallet_service.mapper;

import javax.annotation.processing.Generated;
import org.wallet_service.dto.response.ActionResponseDTO;
import org.wallet_service.entity.Action;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-30T16:13:57+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
public class ActionResponseMapperImpl implements ActionResponseMapper {

    @Override
    public ActionResponseDTO ActionToActionResponseTO(Action action) {
        if ( action == null ) {
            return null;
        }

        ActionResponseDTO actionResponseDTO = new ActionResponseDTO();

        actionResponseDTO.setDateTime( action.getDateTime() );
        actionResponseDTO.setMessage( action.getMessage() );

        return actionResponseDTO;
    }
}
