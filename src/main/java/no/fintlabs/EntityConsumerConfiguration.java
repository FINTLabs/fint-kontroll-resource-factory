package no.fintlabs;

import no.fint.model.resource.FintLinks;
import no.fintlabs.applicationResource.ApplicationResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.ApplikasjonResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisenstilgangResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.LisensmodellResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.PlattformResource;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import no.fintlabs.links.ResourceLinkUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
public class EntityConsumerConfiguration {
    private final EntityConsumerFactoryService entityConsumerFactoryService;

    public EntityConsumerConfiguration(EntityConsumerFactoryService entityConsumerFactoryService) {
        this.entityConsumerFactoryService = entityConsumerFactoryService;
    }

    private <T extends FintLinks> ConcurrentMessageListenerContainer<String, T> createCacheConsumer(
            String resourceReference, Class<T> resourceClass, FintCache<String, T> cache ) {
        return entityConsumerFactoryService.createRecordConsumerFactory(
                resourceClass,
                consumerRecord -> cache.put(
                        ResourceLinkUtil.getSelfLinks(consumerRecord .value()),
                        consumerRecord.value()
                )
        ).createContainer(EntityTopicNameParameters.builder().resource(resourceReference).build());
    }

    @Bean
    ConcurrentMessageListenerContainer<String, LisensResource> lisensResourceConcurrentMessageListenerContainer(
            FintCache<String, LisensResource> lisensResourceFintCache ) {
        return createCacheConsumer(
                "eiendeler.applikasjon.lisens",
                LisensResource.class,
                lisensResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, LisenstilgangResource> lisenstilgangResourceConcurrentMessageListenerContainer(
            FintCache<String,LisenstilgangResource> lisenstilgangResourceFintCache ){
        return createCacheConsumer(
                "eiendeler.applikasjon.lisenstilgang",
                LisenstilgangResource.class,
                lisenstilgangResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ApplikasjonResource> applikasjonResourceConcurrentMessageListenerContainer(
            FintCache<String,ApplikasjonResource> applikasjonResourceFintCache ) {
        return createCacheConsumer(
                "eiendeler.applikasjon.applikasjon",
                ApplikasjonResource.class,
                applikasjonResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, OrganisasjonselementResource> organisasjonselementResourceEntityConsumer(
            FintCache<String, OrganisasjonselementResource> organisasjonselementResourceCache ) {
        return createCacheConsumer(
                "administrasjon.organisasjon.organisasjonselement",
                OrganisasjonselementResource.class,
                organisasjonselementResourceCache
        );
    }



    @Bean
    ConcurrentMessageListenerContainer<String, BrukertypeResource> brukertypeResourceConcurrentMessageListenerContainer(
            FintCache<String, BrukertypeResource> brukertypeResourceFintCache ) {
        return createCacheConsumer(
                "eiendeler.kodeverk.brukertype",
                BrukertypeResource.class,
                brukertypeResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, LisensmodellResource> lisensmodellResourceConcurrentMessageListenerContainer(
            FintCache<String, LisensmodellResource> lisensmodellResourceFintCache ) {
        return createCacheConsumer(
                "eiendeler.kodeverk.lisensmodell",
                LisensmodellResource.class,
                lisensmodellResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, PlattformResource> plattformResourceConcurrentMessageListenerContainer(
            FintCache<String, PlattformResource> plattformResourceFintCache ) {
        return createCacheConsumer(
                "eiendeler.kodeverk.plattform",
                PlattformResource.class,
                plattformResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ApplicationResource> applicationResourceEntityConsumer(
            FintCache<String, Integer> publishedApplicationResourceHashCache ) {
        ConcurrentMessageListenerContainer<String, ApplicationResource> applicationresourceConcurentMessageListenerContainer =
                entityConsumerFactoryService.createRecordConsumerFactory(
                ApplicationResource.class,
                consumerRecord -> publishedApplicationResourceHashCache.put(
                        consumerRecord.value().getResourceId(),
                        consumerRecord.value().hashCode()
                )
        ).createContainer(EntityTopicNameParameters.builder().resource("applicationresource").build());

        return applicationresourceConcurentMessageListenerContainer;
    }
}
