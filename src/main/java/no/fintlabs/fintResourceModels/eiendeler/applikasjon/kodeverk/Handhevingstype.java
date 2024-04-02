package no.fintlabs.fintResourceModels.eiendeler.applikasjon.kodeverk;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.fint.model.FintMainObject;
import no.fint.model.felles.basisklasser.Begrep;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class Handhevingstype extends Begrep implements FintMainObject{
    public enum Relasjonsnavn {
        HANDHEVINGSTYPE
    }

    private Identifikator systemId;
    private String kode;
    private String navn;
}
