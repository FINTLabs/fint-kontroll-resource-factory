package no.fintlabs.applicationResourceLocation;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisenstilgangResource;
import no.fintlabs.fintResourceServices.FintResourceLisensService;
import no.fintlabs.fintResourceServices.FintResourceOrgenhetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static no.fintlabs.links.ResourceLinkUtil.getIdentifikatorValueFromPath;

@Service
@Slf4j
public class ApplicationResourceLocationService {
    private final FintCache<String, LisenstilgangResource> lisenstilgangResourceFintCache;
    private final FintResourceOrgenhetService fintResourceOrgenhetService;
    private final FintResourceLisensService fintResourceLisensService;

    public ApplicationResourceLocationService(
            FintCache<String,
                    LisenstilgangResource> lisenstilgangResourceFintCache,
            FintResourceOrgenhetService fintResourceOrgenhetService,
            FintResourceLisensService fintResourceLisensService
    ) {
        this.lisenstilgangResourceFintCache = lisenstilgangResourceFintCache;
        this.fintResourceOrgenhetService = fintResourceOrgenhetService;
        this.fintResourceLisensService = fintResourceLisensService;
    }

    public List<ApplicationResourceLocation> getAllApplicationResourceLocations() {
        Optional<List<LisenstilgangResource>> lisenstilgangResources =  Optional.of(lisenstilgangResourceFintCache.getAll());

        if (lisenstilgangResources.isEmpty()) {
            log.warn("No Lisenstilgangsressurs found");
            return List.of();
        }
        log.info("Found {} lisenstilgang resources ", lisenstilgangResources.get().size());

        return lisenstilgangResources.get()
                .stream()
                .map(this::createApplicationResourceLocation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ApplicationResourceLocation> createApplicationResourceLocation(LisenstilgangResource lisenstilgangResource) {

        if (lisenstilgangResource.getLisens() == null || lisenstilgangResource.getLisenskonsument() == null) {

            log.warn("Creating application resource location failed because lisenstilgangResource is invalid: {} ", lisenstilgangResource);
            return Optional.empty();
        }
        Link lisensLink = lisenstilgangResource.getLisens().getFirst();
        String resourceId = fintResourceLisensService.getLisensResourceSystemId(lisensLink);
        String resourceName = fintResourceLisensService.getLisensResourceName(lisensLink);
        Link orgUnitLink = lisenstilgangResource.getLisenskonsument().getFirst();
        String orgUnitName = fintResourceOrgenhetService.getOrgUnitName(orgUnitLink);
        String orgUnitId = fintResourceOrgenhetService.getOrgUnitId(orgUnitLink);

        if (resourceId == null || resourceId.isEmpty()) {
            log.warn("Creating application resource location failed because application resource could not be found for {}", lisenstilgangResource);
            return Optional.empty();
        }
        if (orgUnitId == null || orgUnitId.isEmpty())
        {

            log.warn("Creating application resource location failed because could orgunit not be found for {}", lisenstilgangResource);
            return Optional.empty();
        }
        return Optional.of(ApplicationResourceLocation
                .builder()
                .resourceId(resourceId)
                .resourceName(resourceName)
                .id(new ApplicationResourceLocationId(null, orgUnitId))
                .orgUnitName(orgUnitName)
                .resourceLimit((long) lisenstilgangResource.getLisensantall())
                .build());
    }

}

