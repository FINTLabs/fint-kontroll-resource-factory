package no.fintlabs.fintResourceModels.eiendeler.applikasjon;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.fint.model.FintAbstractObject;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Lisens implements FintAbstractObject {
    public enum Relasjonsnavn {
        APPLIKASJON,
        LISENSTYPE,
        TILGJENGELIGFORBRUKERTYPE,
        LISENSEIER,
        LISENSTILGANG
    }
    private Identifikator systemid;
    private String lisensnavn;
    private int lisensantall;
    private String beskrivelse;
}
