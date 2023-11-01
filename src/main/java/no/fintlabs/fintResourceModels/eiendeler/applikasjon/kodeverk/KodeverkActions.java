package no.fintlabs.fintResourceModels.eiendeler.applikasjon.kodeverk;

import no.fintlabs.fintResourceModels.eiendeler.applikasjon.ApplikasjonActions;

import java.util.Arrays;
import java.util.List;

public enum KodeverkActions {
    GET_TILGJENGELIGFORBRUKERTYPE,
    GET_ALL_TILGJENGELIGFORBRUKERTYPE,
    UPDATE_TILGJENGELIGFORBRUKERTYPE,
    GET_LISENSMODELL,
    GET_ALL_LISENSMODELL,
    UPDATE_LISENSMODELL,
    GET_STOTTETPLATFORM,
    GET_ALL_STOTTETPLATFORM,
    UPDATE_STOTTETPLATFORM
    ;

    public static List<String> getActions() {
        return Arrays.asList(
                Arrays.stream(ApplikasjonActions.class.getEnumConstants()).map(Enum::name).toArray(java.lang.String[]::new)
        );
    }
}
