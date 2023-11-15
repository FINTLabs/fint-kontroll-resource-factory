package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.ApplikasjonResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.ApplikasjonskategoriResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FintResourceApplikasjonsKategoriService {
    private final FintCache<String, ApplikasjonskategoriResource> applikasjonskategoriResourceFintCache;
    private final FintCache<String, ApplikasjonResource> applikasjonResourceFintCache;

    public FintResourceApplikasjonsKategoriService(
            FintCache<String, ApplikasjonskategoriResource> applikasjonskategoriResourceFintCache,
            FintCache<String, ApplikasjonResource> applikasjonResourceFintCache
    ) {
        this.applikasjonskategoriResourceFintCache = applikasjonskategoriResourceFintCache;
        this.applikasjonResourceFintCache = applikasjonResourceFintCache;
    }

    public List<String> getApplikasjonskategori(LisensResource lisensResource) {
       String applikasjonResourceHref = lisensResource.getApplikasjon().get(0).getHref().toLowerCase();
       Optional<ApplikasjonResource> optionalApplikasjonResource = applikasjonResourceFintCache.getOptional(applikasjonResourceHref);

       if (optionalApplikasjonResource.isEmpty()) {
            List<String> list = new ArrayList<String>();
            list.add("Ingen applikasjonskategori satt");
            return list;
       }

       return optionalApplikasjonResource
               .get()
               .getApplikasjonskategori()
               .stream()
               .map(Link::getHref)
               .map(String::toLowerCase)
               .map(applikasjonskategoriResourceFintCache::getOptional)
               .filter(Optional::isPresent)
               .map(Optional::get)
               .map(ApplikasjonskategoriResource::getNavn)
               .toList();
    }
}