package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.ApplikasjonResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.PlattformResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FintResourcePlattformService {
    private final FintCache<String, PlattformResource> plattformResourceFintCache;
    private final FintCache<String, ApplikasjonResource> applikasjonResourceFintCache;

    public FintResourcePlattformService(FintCache<String, PlattformResource> plattformResourceFintCache,
                                        FintCache<String, ApplikasjonResource> applikasjonResourceFintCache) {
        this.plattformResourceFintCache = plattformResourceFintCache;
        this.applikasjonResourceFintCache = applikasjonResourceFintCache;
    }

    public List<String> getPlatform(LisensResource lisensResource) {

        String applikasjonResourceHref = lisensResource.getApplikasjon().get(0).getHref().toLowerCase();
        Optional<ApplikasjonResource> optionalApplikasjonResource = applikasjonResourceFintCache.getOptional(applikasjonResourceHref);

        if (optionalApplikasjonResource.isEmpty()) {
            List<String> list = new ArrayList<String>();
            list.add("Lisens ikke tilknyttet applikasjon");
            return list;
        }
        ApplikasjonResource applikasjonResource = optionalApplikasjonResource.get();

        if (applikasjonResource.getStottetplattform().isEmpty()) {
            List<String> list = new ArrayList<>();
            list.add("Plattform ikke satt");
            return list;
        }
        return applikasjonResource.getStottetplattform()
                .stream()
                .map(Link::getHref)
                .map(plattformResourceFintCache::getOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PlattformResource::getKode)
                .toList();
    }
}