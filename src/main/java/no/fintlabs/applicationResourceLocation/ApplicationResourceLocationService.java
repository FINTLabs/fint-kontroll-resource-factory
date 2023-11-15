package no.fintlabs.applicationResourceLocation;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisenstilgangResource;
import no.fintlabs.fintResourceServices.FintResourceOrgenhetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static no.fintlabs.links.ResourceLinkUtil.identifikatorNameToLowerCase;

@Service
@Slf4j
public class ApplicationResourceLocationService {
    private final FintCache<String, LisenstilgangResource> lisenstilgangResourceFintCache;
    private final FintResourceOrgenhetService fintResourceOrgenhetService;

    public ApplicationResourceLocationService(FintCache<String, LisenstilgangResource> lisenstilgangResourceFintCache, FintResourceOrgenhetService fintResourceOrgenhetService) {
        this.lisenstilgangResourceFintCache = lisenstilgangResourceFintCache;
        this.fintResourceOrgenhetService = fintResourceOrgenhetService;
    }

    public List<ApplicationResourceLocation> getValidForOrgunits(LisensResource lisensResource) {
        List<Link> lisenstilgangLinker = lisensResource.getLisenstilgang();
        String resourceid = lisensResource.getSystemId().getIdentifikatorverdi();

        return lisenstilgangLinker
                .stream()
                .map(link -> this.createApplicationResourceLocation(link, resourceid))
                .toList();
    }

    private ApplicationResourceLocation createApplicationResourceLocation(Link link, String resourceid) {
        Optional<LisenstilgangResource> optionalLisenstilgangResource = lisenstilgangResourceFintCache
                .getOptional(identifikatorNameToLowerCase(link.getHref()));

        if (!optionalLisenstilgangResource.isEmpty()) {

            LisenstilgangResource lisenstilgangResource = optionalLisenstilgangResource.get();
            Link orgUnitLink = lisenstilgangResource.getLisenskonsument().get(0);
            String orgUnitName = fintResourceOrgenhetService.getOrgUnitName(orgUnitLink);
            String orgUnitId = fintResourceOrgenhetService.getOrgUnitId(orgUnitLink);

            return ApplicationResourceLocation
                    .builder()
                    .resourceId(resourceid)
                    .orgUnitId(orgUnitId)
                    .orgUnitName(orgUnitName)
                    .resourceLimit((long) lisenstilgangResource.getLisensantall())
                    .build();
        } else {
            log.info("Lisenstilgangsressurs " + link.getHref().toLowerCase() + " not found");
            return new ApplicationResourceLocation();
        }
    }
}

