package no.fintlabs.fintResourceModels;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;


import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EiendelerApplikasjonApplikasjon implements FintMainObject,FintLinks{

    private Identifikator systemid;
    private String applikasjonsnavn;


    // Relations
    @Getter
    private Map<String, List<Link>> links = createLinks();

    @JsonIgnore
    public List<Link> getLisens(){
        return getLisens().getOrDefault("lisens", Collections.emptyList());
    }
    public void addLisens(Link link){
        addLink("lisens",link);
    }

    @Override
    public Map<String, List<Link>> getLinks() {
        return null;
    }
}
