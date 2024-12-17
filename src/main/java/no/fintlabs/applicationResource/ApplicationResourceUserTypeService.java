package no.fintlabs.applicationResource;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import no.fintlabs.fintResourceServices.FintResourceBrukertypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApplicationResourceUserTypeService {
    private final  ApplicationResourceConfiguration applicationResourceConfiguration;
    private final FintResourceBrukertypeService fintResourceBrukertypeService;

    public ApplicationResourceUserTypeService(ApplicationResourceConfiguration applicationResourceConfiguration, FintResourceBrukertypeService fintResourceBrukertypeService) {
        this.applicationResourceConfiguration = applicationResourceConfiguration;
        this.fintResourceBrukertypeService = fintResourceBrukertypeService;
    }

    public List<ApplicationResourceUserType> getAllApplicationResourceUserTypes() {
        Optional<List<BrukertypeResource>> brukertypeResources = fintResourceBrukertypeService.getAllBrukertypeResources();

        if (brukertypeResources.isEmpty()) {
            log.warn("No brukertype resources found");
            return List.of();
        }
        return brukertypeResources.get()
                .stream()
                .map(this::createApplicationResourceUserType)
                .toList();
    }
    private ApplicationResourceUserType createApplicationResourceUserType(BrukertypeResource brukertypeResource) {
        String internalUserType = ValidForRolesMapping.mapValidForRolesToUserTypes(
                List.of(brukertypeResource.getSystemId().getIdentifikatorverdi()), applicationResourceConfiguration).getFirst();
        return new ApplicationResourceUserType(internalUserType, brukertypeResource.getNavn());
    }
}
