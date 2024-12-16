package no.fintlabs.applicationResource;

import no.fint.model.resource.Link;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocationService;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceServices.*;
import no.fintlabs.kodeverk.Handhevingstype;
import no.fintlabs.kodeverk.Brukertype;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.cache.FintCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApplicationResourceService {
    private final  ApplicationResourceConfiguration applicationResourceConfiguration;
    private final FintCache<String, LisensResource> lisensResourceFintCache;
    private final FintResourceLisensService fintResourceLisensService;
    private final FintResourcePlattformService fintResourcePlattformService;
    private final FintResourceBrukertypeService fintResourceBrukertypeService;
    private final FintResourceLisensmodelService fintResourceLisensmodelService;
    private final FintResourceApplikasjonsKategoriService fintResourceApplikasjonsKategoriService;
    private final ApplicationResourceLocationService applicationResourceLocationService;
    private final ApplicationResourceEntityProducerService applicationResourceEntityProducerService;

    public ApplicationResourceService(ApplicationResourceConfiguration applicationResourceConfiguration,
                                      FintCache<String, LisensResource> lisensResourceFintCache,
                                      FintResourceLisensService fintResourceLisensService,
                                      FintResourcePlattformService fintResourcePlattformService,
                                      FintResourceBrukertypeService fintResourceBrukertypeService,
                                      FintResourceLisensmodelService fintResourceLisensmodelService,
                                      ApplicationResourceLocationService applicationResourceLocationService,
                                      ApplicationResourceEntityProducerService applicationResourceEntityProducerService,
                                      FintResourceApplikasjonsKategoriService fintResourceApplikasjonsKategoriService) {
        this.applicationResourceConfiguration = applicationResourceConfiguration;
        this.lisensResourceFintCache = lisensResourceFintCache;
        this.fintResourceLisensService = fintResourceLisensService;
        this.fintResourcePlattformService = fintResourcePlattformService;
        this.fintResourceBrukertypeService = fintResourceBrukertypeService;
        this.fintResourceLisensmodelService = fintResourceLisensmodelService;
        this.applicationResourceLocationService = applicationResourceLocationService;
        this.applicationResourceEntityProducerService = applicationResourceEntityProducerService;
        this.fintResourceApplikasjonsKategoriService = fintResourceApplikasjonsKategoriService;
    }


    public List<ApplicationResource> getAllApplicationResources() {
        return lisensResourceFintCache.getAllDistinct()
                .stream()
                .filter(lisensResource -> !lisensResource.getLisenstilgang().isEmpty())
                .filter(lisensResource -> !lisensResource.getApplikasjon().isEmpty())
                .map(this::createApplicationResource)
                .filter(Optional::isPresent)
                .map(Optional::get)
                //.filter(applicationResource -> ! applicationResource.getApplicationCategory().contains("Pedagogisk verktøy"))
                .toList();
     }

    private Optional<ApplicationResource> createApplicationResource(LisensResource lisensResource) {

        ApplicationResource applicationResource = new ApplicationResource();

        applicationResource.setResourceId(lisensResource.getSystemId().getIdentifikatorverdi());
        applicationResource.setResourceName(lisensResource.getLisensnavn());
        applicationResource.setResourceType("ApplicationResource");
        applicationResource.setResourceOwnerOrgUnitId(fintResourceLisensService.getResourceOwnerOrgUnitId(lisensResource));
        applicationResource.setResourceOwnerOrgUnitName(fintResourceLisensService.getResourceOwnerOrgUnitName(lisensResource));
        applicationResource.setResourceLimit(fintResourceLisensService.getResourceLimit(lisensResource));
        //applicationResource.setPlatform(fintResourcePlattformService.getPlatform(lisensResource));
        applicationResource.setAccessType(fintResourceLisensmodelService.getAccessType(lisensResource));
        applicationResource.setValidForRoles(ValidForRolesMapping.mapValidForRolesToUserTypes(fintResourceBrukertypeService.getAvailableForUsertypeIds(lisensResource), applicationResourceConfiguration));
        //applicationResource.setUserTypes(mapValidForRolesToUserTypes(fintResourceBrukertypeService.getAvailableForUsertypeIds(lisensResource)));
        applicationResource.setValidForOrgUnits(applicationResourceLocationService.getValidForOrgunits(lisensResource));
        applicationResource.setApplicationCategory(fintResourceApplikasjonsKategoriService.getApplikasjonskategori(lisensResource));
        // Nye felter 3.18
        //applicationResource.setLicenseModel(fintResourceLisensmodelService.getLicenseModel(lisensResource));
        applicationResource.setLicenseEnforcement(LicenseModelMapping.mapLicenseModelToLicenseEnforcement(lisensResource, applicationResourceConfiguration));
        applicationResource.setStatus("ACTIVE");
//        applicationResource.setStatusChanged();
        return Optional.of(applicationResource);
    }
}
