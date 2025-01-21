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

    public ApplicationResourceUserTypeService(
            ApplicationResourceConfiguration applicationResourceConfiguration,
            FintResourceBrukertypeService fintResourceBrukertypeService
    ) {
        this.applicationResourceConfiguration = applicationResourceConfiguration;
        this.fintResourceBrukertypeService = fintResourceBrukertypeService;
    }

    public List<ApplicationResourceUserType> getAllApplicationResourceUserTypes() {
        Optional<List<BrukertypeResource>> brukertypeResources = fintResourceBrukertypeService.getAllBrukertypeResources();

        if (brukertypeResources.isEmpty()) {
            log.warn("No brukertype resources found");
            return List.of();
        }
        log.info("Found {} brukertype resources", brukertypeResources.get().size());
        log.info("Brukertype resource info: {}", brukertypeResources.get()
                .stream()
                .map(brukertypeResource ->
                        brukertypeResource.getSystemId().getIdentifikatorverdi()+
                        ' '+brukertypeResource.getNavn())
                .toList()
        );

        return brukertypeResources.get()
                .stream()
                .map(this::createApplicationResourceUserType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ApplicationResourceUserType> createApplicationResourceUserType (BrukertypeResource brukertypeResource) {
        String internalUserType = ValidForUserTypesMapping.mapExternalToInternalUserType(
                brukertypeResource.getSystemId().getIdentifikatorverdi(), applicationResourceConfiguration);

        if (internalUserType==null) {
            log.warn("No internal user type found for brukertype resource with systemId: {}", brukertypeResource.getSystemId().getIdentifikatorverdi());
            return Optional.empty();
        }
        log.info("Mapping brukertype resource with systemId {} to internal user type {}", brukertypeResource.getSystemId().getIdentifikatorverdi(), internalUserType);
        return Optional.of(new ApplicationResourceUserType(internalUserType, brukertypeResource.getNavn()));
    }
}
