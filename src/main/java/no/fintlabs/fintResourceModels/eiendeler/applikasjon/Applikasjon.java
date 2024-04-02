package no.fintlabs.fintResourceModels.eiendeler.applikasjon;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.fint.model.FintAbstractObject;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Applikasjon implements FintAbstractObject {
    public enum Releasjonsnavn {
        LISENS,
        APPLIKASJONSKATEGORI,
        STOTTETPLATTFORM
    }
    private Identifikator systemId;
    private String applikasjonsnavn;
    private String beskrivelse;
    private Periode gyldighetsperiode;
}
