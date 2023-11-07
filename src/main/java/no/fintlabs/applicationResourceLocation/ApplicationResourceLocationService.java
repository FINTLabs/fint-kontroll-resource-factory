package no.fintlabs.applicationResourceLocation;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisenstilgangResource;
import no.fintlabs.fintResourceServices.FintResourceOrgenhetService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        String resourceid = lisensResource.getSystemid().getIdentifikatorverdi();

        return lisenstilgangLinker
                .stream()
                .map(link -> this.createApplicationResourceLocation(link, resourceid))
                .toList();


    }

    private ApplicationResourceLocation createApplicationResourceLocation(Link link, String resourceid) {
        LisenstilgangResource lisenstilgangResource = lisenstilgangResourceFintCache
                .getOptional(link.getHref())
                .orElse(new LisenstilgangResource());

        Link orgUnitLink = lisenstilgangResource.getLisenskonsument().get(0);

        String orgUnitName = fintResourceOrgenhetService.getOrgUnitName(link);
        String orgUnitId = fintResourceOrgenhetService.getOrgUnitId(link);


        return ApplicationResourceLocation
                .builder()
                .resourceId(resourceid)
                .orgUnitId(orgUnitId)
                .orgUnitName(orgUnitName)
                .resourceLimit((long) lisenstilgangResource.getLisensantall())
                .build();
    }
}


//
//public class ApplicationResourceLocation {
//    private Long id;
//    private String resourceId;
//    private String orgUnitId;
//    private String orgUnitName;
//    private Long resourceLimit;
//}
