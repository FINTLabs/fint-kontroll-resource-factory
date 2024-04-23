package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.ApplikasjonResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.ApplikasjonskategoriResource;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FintResourceApplikasjonsKategoriService {
    private final FintCache<String, ApplikasjonskategoriResource> applikasjonskategoriResourceFintCache;
    private final FintCache<String, ApplikasjonResource> applikasjonResourceFintCache;
    private final ApplicationCategoryProduserService applicationCategoryProduserService;

    public FintResourceApplikasjonsKategoriService(
            FintCache<String, ApplikasjonskategoriResource> applikasjonskategoriResourceFintCache,
            FintCache<String, ApplikasjonResource> applikasjonResourceFintCache, ApplicationCategoryProduserService applicationCategoryProduserService
    ) {
        this.applikasjonskategoriResourceFintCache = applikasjonskategoriResourceFintCache;
        this.applikasjonResourceFintCache = applikasjonResourceFintCache;
        this.applicationCategoryProduserService = applicationCategoryProduserService;
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

@Scheduled(initialDelay = 5000, fixedDelay = 50000)
    public Map<String,String> getAllApplicationCategoriesAndPublish(){
        Map<String,String> applicationCategories = new HashMap<>();
        applicationCategories = applikasjonskategoriResourceFintCache.getAllDistinct()
                .stream()
                .map(this::getApplicationCategory)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> replacement
                ));
        applicationCategoryProduserService.publish(applicationCategories);


        return applicationCategories;
    }

    public Map<String,String> getApplicationCategory(ApplikasjonskategoriResource applikasjonskategoriResource){
        Map<String,String> applicationCategory = new HashMap<>();
        applicationCategory.put(applikasjonskategoriResource.getSystemId().getIdentifikatorverdi(), applikasjonskategoriResource.getNavn());
        return applicationCategory;
    }
}
