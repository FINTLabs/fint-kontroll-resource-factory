package no.fintlabs;

import no.fintlabs.applicationResource.ApplicationResourceUserType;
import no.fintlabs.cache.FintCache;
import no.fintlabs.cache.FintCacheManager;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.ApplikasjonResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisenstilgangResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.ApplikasjonskategoriResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.LisensmodellResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.PlattformResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class EntityCacheConfiguration {
    private final FintCacheManager fintCacheManager;

    public EntityCacheConfiguration(FintCacheManager fintCacheManager) {
        this.fintCacheManager = fintCacheManager;
    }
    @Bean
    FintCache<String, ApplikasjonResource> applikasjonResourceFintCache(){
        return createCache(ApplikasjonResource.class);
    }

    @Bean
    FintCache<String, LisensResource> lisensResourceFintCache(){
        return createCache(LisensResource.class);
    }

    @Bean
    FintCache<String, LisenstilgangResource> lisenstilgangResourceFintCache(){
        return createCache(LisenstilgangResource.class);
    }

    @Bean
    FintCache<String, BrukertypeResource> brukertypeResourceFintCache(){
        return createCache(BrukertypeResource.class);
    }

    @Bean
    FintCache<String, LisensmodellResource> lisensmodellResourceFintCache(){
        return createCache(LisensmodellResource.class);
    }

    @Bean
    FintCache<String, PlattformResource> plattformResourceFintCache(){
        return createCache(PlattformResource.class);
    }
    @Bean
    FintCache<String, ApplikasjonskategoriResource> applikasjonskategoriformResourceFintCache(){
        return createCache(ApplikasjonskategoriResource.class);
    }
    @Bean
    FintCache<String, OrganisasjonselementResource> organisasjonselementResourceCache() {
        return createCache(OrganisasjonselementResource.class);
    }

    @Bean
    FintCache<String, Integer> publishedApplicationResourceHashCache() {
        return createCache(Integer.class);
    }
    @Bean
    FintCache<String, ApplicationResourceUserType> publishedApplicationResourceUserCache() {
        return createCache(ApplicationResourceUserType.class);
    }


    private <V> FintCache<String, V> createCache(Class<V> resourceClass) {
        return fintCacheManager.createCache(
                resourceClass.getName().toLowerCase(Locale.ROOT),
                String.class,
                resourceClass
        );
    }


}
