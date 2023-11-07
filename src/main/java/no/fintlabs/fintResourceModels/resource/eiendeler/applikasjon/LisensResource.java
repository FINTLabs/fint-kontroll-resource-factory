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
public class LisensResource implements FintMainObject, FintLinks {
    private Identifikator systemid;
    private String lisensnavn;
    private int lisensantall;
    private String beskrivelse;

    //References
    @Getter
    private final Map<String, List<Link>> links = createLinks();

    @JsonIgnore
    public List<Link> getApplikasjon(){
        return getLinks().getOrDefault("applikasjon", Collections.emptyList());
    }
    public void addApplikasjon(Link link){
        addLink("applikasjon",link);
    }

    @JsonIgnore
    public List<Link> getLisensmodell(){return getLinks().getOrDefault("lisensmodell", Collections.emptyList());}
    public void addLisensmodell(Link link){addLink("lisensmodell",link);}

    @JsonIgnore
    public List<Link> getTilgjengeligforbrukertype(){return getLinks().getOrDefault("tilgjengeligforbrukertype", Collections.emptyList());}
    public void addTilgjengeligforbrukertype(Link link){addLink("tilgjengeligforbrukertype",link);}

    @JsonIgnore
    public List<Link> getLisenseier(){return getLinks().getOrDefault("lisenseier", Collections.emptyList());}
    public void addLisenseier(Link link){addLink("lisenseier",link);}

    @JsonIgnore
    public List<Link> getLisenstilgang(){return getLinks().getOrDefault("lisenstilgang", Collections.emptyList());}
    public void addLisenstilgang(Link link){addLink("lisenstilgang",link);}



}
