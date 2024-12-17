package no.fintlabs.applicationResource;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import no.fintlabs.fintResourceServices.FintResourceBrukertypeService;
import no.fintlabs.kodeverk.Brukertype;
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
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ApplicationResourceUserType> createApplicationResourceUserType (BrukertypeResource brukertypeResource) {
        List<String> internalUserTypes = ValidForRolesMapping.mapValidForRolesToUserTypes(
                List.of(brukertypeResource.getSystemId().getIdentifikatorverdi()), applicationResourceConfiguration);
        if (internalUserTypes.isEmpty()) {
            log.warn("No internal user type found for brukertype resource with systemId: {}", brukertypeResource.getSystemId().getIdentifikatorverdi());
            return Optional.empty();
        }
        String internalUserType = internalUserTypes.contains(Brukertype.ALLTYPES.name()) ? Brukertype.ALLTYPES.name() : internalUserTypes.getFirst();

        return Optional.of(new ApplicationResourceUserType(internalUserType, brukertypeResource.getNavn()));
    }
}
