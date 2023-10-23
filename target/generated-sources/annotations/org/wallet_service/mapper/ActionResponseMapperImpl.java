package org.wallet_service.mapper;

import javax.annotation.processing.Generated;
import org.wallet_service.dto.response.ActionResponseTO;
import org.wallet_service.entity.Action;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-23T17:38:15+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.3.1 (Oracle Corporation)"
)
public class ActionResponseMapperImpl implements ActionResponseMapper {

    @Override
    public ActionResponseTO ActionToActionResponseTO(Action action) {
        if ( action == null ) {
            return null;
        }

        ActionResponseTO actionResponseTO = new ActionResponseTO();

        actionResponseTO.setDateTime( action.getDateTime() );
        actionResponseTO.setMessage( action.getMessage() );

        return actionResponseTO;
    }
}
