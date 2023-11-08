package no.fintlabs.applicationResource;

import no.fintlabs.cache.FintCache;
import no.fintlabs.kafka.entity.EntityProducer;
import no.fintlabs.kafka.entity.EntityProducerFactory;
import no.fintlabs.kafka.entity.EntityProducerRecord;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import no.fintlabs.kafka.entity.topic.EntityTopicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationResourceEntityProducerService {
    private final EntityProducer<ApplicationResource> entityProducer;
    private final EntityTopicNameParameters entityTopicNameParameters;

    private final FintCache<String, Integer> publishedApplicationResourceHashCache;

    public ApplicationResourceEntityProducerService(
            EntityProducerFactory entityProducerFactory,
            EntityTopicService entityTopicService,
            FintCache<String, Integer> publishedApplicationResourceHashCache
    ) {
        entityProducer = entityProducerFactory.createProducer(ApplicationResource.class);
        this.publishedApplicationResourceHashCache = publishedApplicationResourceHashCache;
        entityTopicNameParameters = EntityTopicNameParameters
                .builder()
                .resource("applicationresource")
                .build();
        entityTopicService.ensureTopic(entityTopicNameParameters, 0);
    }


    public List<ApplicationResource> publish(List<ApplicationResource> applicationResources) {

        return applicationResources
                .stream()
                .filter(appRes -> publishedApplicationResourceHashCache
                        .getOptional(appRes.getResourceId())
                        .map(publishedAppResHash -> publishedAppResHash != appRes.hashCode())
                        .orElse(true)
                )
                .peek(this::publish)
                .toList();
    }

    public void publish(ApplicationResource applicationResource) {
        String key = applicationResource.getResourceId();
        entityProducer.send(
                EntityProducerRecord.<ApplicationResource>builder()
                        .topicNameParameters(entityTopicNameParameters)
                        .key(key)
                        .value(applicationResource)
                        .build()
        );
    }
}
