package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocation;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FintResourceLisenstilgangService {
    public List<ApplicationResourceLocation> getLisenstilganger(LisensResource lisensResource) {
        return null;
    }
}
