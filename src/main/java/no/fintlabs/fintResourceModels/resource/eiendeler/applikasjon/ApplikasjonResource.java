package no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fintlabs.fintResourceModels.eiendeler.applikasjon.Lisens;
import no.fintlabs.fintResourceModels.eiendeler.applikasjon.Applikasjon;


import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ApplikasjonResource implements FintMainObject,FintLinks{

    private Identifikator systemId;
    private String applikasjonsnavn;
    private String beskrivelse;
    private Periode gyldighetsperiode;


    // Relations
    @Getter
    private final Map<String, List<Link>> links = createLinks();

    @JsonIgnore
    public List<Link> getLisens(){
        return getLinks().getOrDefault("lisens", Collections.emptyList());
    }
    public void addLisens(Link link){
        addLink("lisens",link);
    }

    @JsonIgnore
    public List<Link> getApplikasjonskategori(){
        return getLinks().getOrDefault("applikasjonskategori", Collections.emptyList());
    }
    public void addApplikasjonskategori(Link link){
        addLink("applikasjonskategori",link);
    }

    @JsonIgnore
    public List<Link> getStottetplattform(){
        return getLinks().getOrDefault("stottetplattform", Collections.emptyList());
    }
    public void addStottetplattform(Link link){
        addLink("stottetplattform",link);
    }

}
