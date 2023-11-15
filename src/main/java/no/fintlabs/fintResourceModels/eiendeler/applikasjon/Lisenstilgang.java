package no.fintlabs.fintResourceModels.eiendeler.applikasjon;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.fint.model.FintAbstractObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Lisenstilgang implements FintAbstractObject {
    public enum Relasjonsnavn {
        LISENSKONSUMENT,
        LISENS,
    }
    private Identifikator systemId;
    private int lisensantall;
}
