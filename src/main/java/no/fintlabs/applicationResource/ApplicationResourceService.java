package no.fintlabs.applicationResource;

import no.fintlabs.applicationResourceLocation.ApplicationResourceLocationService;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceServices.*;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.ApplikasjonResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApplicationResourceService {
    private final FintCache<String, ApplikasjonResource> applikasjonResourceFintCache;
    private final FintCache<String, LisensResource> lisensResourceFintCache;
    private final FintResourceOrgenhetService fintResourceOrgenhetService;

    private final FintResourceLisensService fintResourceLisensService;
    private final FintResourcePlattformService fintResourcePlattformService;
    private final FintResourceBrukertypeService fintResourceBrukertypeService;
    private final FintResourceLisensmodelService fintResourceLisensmodelService;
    private final FintResourceLisenstilgangService fintResourceLisenstilgangService;
    private final ApplicationResourceLocationService applicationResourceLocationService;

    public ApplicationResourceService(FintCache<String, ApplikasjonResource> applikasjonResourceFintCache, FintCache<String, LisensResource> lisensResourceFintCache, FintResourceOrgenhetService fintResourceOrgenhetService, FintResourceLisensService fintResourceLisensService, FintResourcePlattformService fintResourcePlattformService, FintResourceBrukertypeService fintResourceBrukertypeService, FintResourceLisensmodelService fintResourceLisensmodelService, FintResourceLisenstilgangService fintResourceLisenstilgangService, ApplicationResourceLocationService applicationResourceLocationService) {
        this.applikasjonResourceFintCache = applikasjonResourceFintCache;
        this.lisensResourceFintCache = lisensResourceFintCache;
        this.fintResourceOrgenhetService = fintResourceOrgenhetService;
        this.fintResourceLisensService = fintResourceLisensService;
        this.fintResourcePlattformService = fintResourcePlattformService;
        this.fintResourceBrukertypeService = fintResourceBrukertypeService;
        this.fintResourceLisensmodelService = fintResourceLisensmodelService;
        this.fintResourceLisenstilgangService = fintResourceLisenstilgangService;
        this.applicationResourceLocationService = applicationResourceLocationService;
    }

    @Scheduled(
            initialDelayString = "${fint.kontroll.user.publishing.initial-delay}",
            fixedDelayString = "${fint.kontroll.user.publishing.fixed-delay}"
    )
    public void toApplicationResourceFromFintObject(){
        List<ApplicationResource> applicationResources = lisensResourceFintCache.getAllDistinct()
                .stream()
                .map(this::createApplicationResource)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        log.info("ApplicationRespource object: " + applicationResources.toString());

    }



    private Optional<ApplicationResource> createApplicationResource(LisensResource lisensResource) {

        ApplicationResource applicationResource = new ApplicationResource();
        applicationResource.setResourceId(lisensResource.getSystemid().getIdentifikatorverdi());
        applicationResource.setResourceOwnerOrgUnitId(fintResourceLisensService.getResourceOwnerOrgUnitId(lisensResource));
        applicationResource.setResourceOwnerOrgUnitName(fintResourceLisensService.getResourceOwnerOrgUnitName(lisensResource));
        applicationResource.setResourceLimit(fintResourceLisensService.getResourceLimit(lisensResource));
        applicationResource.setPlatform(fintResourcePlattformService.getPlatform(lisensResource));
        applicationResource.setAccessType(fintResourceLisensmodelService.getAccessType(lisensResource));
        applicationResource.setValidForRoles(fintResourceBrukertypeService.getValidForRoles(lisensResource));

        applicationResource.setValidForOrgUnits(applicationResourceLocationService.getValidForOrgunits(lisensResource));




//                  ** Avventer **
//               -  applicationAccessType("") Applikasjonstilgang eller applikasjonsrolletilgang.
//               -  applicationAccessRole("") Evt rolle til intern tilgang i app. Ref qlik. applicationAccesstype må være
//                  "applikasjonsrolletilgang for at dette feltet er fylt ut"
//               -  applicationCategory





        return Optional.of(new ApplicationResource());
    }

}
