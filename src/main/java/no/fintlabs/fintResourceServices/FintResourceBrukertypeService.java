package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FintResourceBrukertypeService {
    private final FintCache<String, BrukertypeResource> brukertypeResourceFintCache;

    public FintResourceBrukertypeService(FintCache<String, BrukertypeResource> brukertypeResourceFintCache) {
        this.brukertypeResourceFintCache = brukertypeResourceFintCache;
    }

    public List<String> getValidForRoles(LisensResource lisensResource) {

        List<Link> tilgjengeligforbrukertypeLinks = lisensResource.getTilgjengeligforbrukertype();

        List<BrukertypeResource> brukertypeResources = tilgjengeligforbrukertypeLinks
                .stream()
                .map(Link::getHref)
                .map(brukertypeResourceFintCache::getOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();



        List<String> validForRoles = brukertypeResources
                .stream()
                .map(BrukertypeResource::getKode)
                .toList();


        return validForRoles;
    }
}
