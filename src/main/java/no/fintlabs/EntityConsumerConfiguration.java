package no.fintlabs;

import no.fint.model.resource.FintLinks;
import no.fintlabs.applicationResource.ApplicationResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.kafka.common.ListenerContainerFactory;
import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import no.fintlabs.kafka.entity.topic.EntityTopicNamePatternParameters;
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






    @Bean
    ConcurrentMessageListenerContainer<String, ApplicationResource> applicationResourceEntityConsumer(
            FintCache<String, Integer> publishedApplicationResourceHashCache
    ) {
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


    @Bean
    ConcurrentMessageListenerContainer<String, OrganisasjonselementResource> organisasjonselementResourceEntityConsumer(
            FintCache<String, OrganisasjonselementResource> organisasjonselementResourceCache
    ) {
        return createCacheConsumer(
                "administrasjon.organisasjon.organisasjonselement",
                OrganisasjonselementResource.class,
                organisasjonselementResourceCache
        );
    }

    private <T extends FintLinks> ConcurrentMessageListenerContainer<String, T> createCacheConsumer(
            String resourceReference,
            Class<T> resourceClass,
            FintCache<String, T> cache
    ) {
        return entityConsumerFactoryService.createRecordConsumerFactory(
                resourceClass,
                consumerRecord -> cache.put(
                        ResourceLinkUtil.getSelfLinks(consumerRecord .value()),
                        consumerRecord.value()
                )
        ).createContainer(EntityTopicNameParameters.builder().resource(resourceReference).build());
    }
}
