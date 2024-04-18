package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.LisensmodellResource;
import no.fintlabs.links.ResourceLinkUtil;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FintResourceLisensmodelService {
    private final FintCache<String, LisensmodellResource> lisensmodellResourceFintCache;

    public FintResourceLisensmodelService(FintCache<String, LisensmodellResource> lisensmodellResourceFintCache) {
        this.lisensmodellResourceFintCache = lisensmodellResourceFintCache;
    }

    public String getAccessType(LisensResource lisensResource) {

        if (lisensResource.getLisensmodell().isEmpty()) {
            return "Ukjent";
        }
        String href = ResourceLinkUtil.systemIdToLowerCase(lisensResource.getLisensmodell().get(0).getHref());

        return lisensmodellResourceFintCache
                .getOptional(href)
                .map(LisensmodellResource::getNavn)
                .orElse("ingen lisensinfo");
    }
}
