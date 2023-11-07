package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.LisensmodellResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class FintResourceLisensmodelService {
    private final FintCache<String, LisensmodellResource> lisensmodellResourceFintCache;

    public FintResourceLisensmodelService(FintCache<String, LisensmodellResource> lisensmodellResourceFintCache) {
        this.lisensmodellResourceFintCache = lisensmodellResourceFintCache;
    }

    public String getAccessType(LisensResource lisensResource) {

        Link lisensModellLink = lisensResource.getLisensmodell().get(0);



        return lisensmodellResourceFintCache
                .getOptional(lisensModellLink.getHref())
                .map(lisensmodellResource -> lisensmodellResource.getKode())
                .orElse("");



    }
}
