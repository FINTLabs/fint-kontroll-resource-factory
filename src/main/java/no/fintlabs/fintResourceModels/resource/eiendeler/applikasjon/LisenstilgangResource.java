package no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LisenstilgangResource implements FintMainObject, FintLinks {

    private Identifikator systemId;
    private int lisensantall;



    @Getter
    private final Map<String, List<Link>> links = createLinks();

    @JsonIgnore
    public List<Link> getLisenskonsument(){
        return getLinks().getOrDefault("lisenskonsument", Collections.emptyList());
    }
    public void addLisenskonsument(Link link){
        addLink("lisenskonsument",link);
    }

    @JsonIgnore
    public List<Link> getLisens(){
        return getLinks().getOrDefault("lisens", Collections.emptyList());
    }
    public void addLisens(Link link){
        addLink("lisens",link);
    }
}
