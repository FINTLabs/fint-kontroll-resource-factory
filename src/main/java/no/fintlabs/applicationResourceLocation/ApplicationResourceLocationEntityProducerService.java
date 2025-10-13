package no.fintlabs.applicationResourceLocation;

import no.fintlabs.cache.FintCache;
import no.fintlabs.kafka.entity.EntityProducer;
import no.fintlabs.kafka.entity.EntityProducerFactory;
import no.fintlabs.kafka.entity.EntityProducerRecord;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import no.fintlabs.kafka.entity.topic.EntityTopicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationResourceLocationEntityProducerService {
    private final EntityProducer<ApplicationResourceLocation> entityProducer;
    private final EntityTopicNameParameters entityTopicNameParameters;

    private final FintCache<String, ApplicationResourceLocation> applicationResourceLocationCache;

    public ApplicationResourceLocationEntityProducerService(
            EntityProducerFactory entityProducerFactory,
            EntityTopicService entityTopicService,
            FintCache<String, ApplicationResourceLocation> applicationResourceLocationCache
    ) {
        entityProducer = entityProducerFactory.createProducer(ApplicationResourceLocation.class);
        this.applicationResourceLocationCache = applicationResourceLocationCache;
        entityTopicNameParameters = EntityTopicNameParameters
                .builder()
                .resource("applicationresource-location")
                .build();
        entityTopicService.ensureTopic(entityTopicNameParameters, 0);
    }


    public List<ApplicationResourceLocation> publish(List<ApplicationResourceLocation> applicationResourceLocations) {

        return applicationResourceLocations
                .stream()
                .filter(resourceLocation -> applicationResourceLocationCache
                        .getOptional(resourceLocation.getResourceId())
                        .map(publishedResourceLocation -> !resourceLocation.equals(publishedResourceLocation))
                        .orElse(true)
                )
                .peek(this::publish)
                .toList();
    }

    private void publish(ApplicationResourceLocation applicationResourceLocation) {
        String key = applicationResourceLocation.getResourceId();
        entityProducer.send(
                EntityProducerRecord.<ApplicationResourceLocation>builder()
                        .topicNameParameters(entityTopicNameParameters)
                        .key(key)
                        .value(applicationResourceLocation)
                        .build()
        );
        applicationResourceLocationCache.put(key, applicationResourceLocation);
    }
}

