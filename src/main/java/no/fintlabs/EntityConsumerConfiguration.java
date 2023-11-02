package no.fintlabs;

import no.fint.model.resource.FintLinks;
import no.fintlabs.applicationResource.ApplicationResource;
import no.fintlabs.cache.FintCache;
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






    @Bean
    ConcurrentMessageListenerContainer<String, ApplicationResource> userEntityConsumer(
            FintCache<String, Integer> publishedApplicationResourceHashCache
    ) {
        return entityConsumerFactoryService.createRecordConsumerFactory(
                ApplicationResource.class,
                consumerRecord -> publishedApplicationResourceHashCache.put(
                        consumerRecord.value().getResourceId(),
                        consumerRecord.value().hashCode()
                )
        ).createContainer(EntityTopicNameParameters.builder().resource("applicationresource").build());
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
