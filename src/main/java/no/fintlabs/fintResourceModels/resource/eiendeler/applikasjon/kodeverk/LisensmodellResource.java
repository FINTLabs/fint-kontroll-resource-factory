package no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import no.fint.model.FintMainObject;
import no.fint.model.felles.basisklasser.Begrep;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class LisensmodellResource extends Begrep implements FintMainObject, FintLinks {
    private Identifikator systemid;
    private String kode;
    private String navn;

    // Relations
    @Getter
    private final Map<String, List<Link>> links = createLinks();

    @JsonIgnore
    public List<Link> getLisensmodell() {
        return getLinks().getOrDefault("lisensmodell", Collections.emptyList());
    }
    public void addLisensmodell(Link link) {
        addLink("lisensmodell", link);
    }


}
