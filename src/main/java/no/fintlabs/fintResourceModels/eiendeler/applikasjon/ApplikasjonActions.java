package no.fintlabs.fintResourceModels.eiendeler.applikasjon;

import java.util.Arrays;
import java.util.List;

public enum ApplikasjonActions {
   ;

    public static List<String> getActions() {
        return Arrays.asList(
                Arrays.stream(ApplikasjonActions.class.getEnumConstants()).map(Enum::name).toArray(java.lang.String[]::new)
        );
    }
}
