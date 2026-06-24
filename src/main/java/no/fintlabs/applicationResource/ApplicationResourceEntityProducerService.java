package no.fintlabs.applicationResource;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.cache.FintCache;
import no.novari.kafka.producing.ParameterizedProducerRecord;
import no.novari.kafka.producing.ParameterizedTemplate;
import no.novari.kafka.producing.ParameterizedTemplateFactory;
import no.novari.kafka.topic.EntityTopicService;
import no.novari.kafka.topic.configuration.EntityCleanupFrequency;
import no.novari.kafka.topic.configuration.EntityTopicConfiguration;
import no.novari.kafka.topic.name.EntityTopicNameParameters;
import no.novari.kafka.topic.name.TopicNamePrefixParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class ApplicationResourceEntityProducerService {
    private final ParameterizedTemplate<ApplicationResource> parameterizedTemplate;
    private final EntityTopicNameParameters entityTopicNameParameters;
    private final FintCache<String, Integer> publishedApplicationResourceHashCache;

    public ApplicationResourceEntityProducerService(
            EntityTopicService entityTopicService,
            ParameterizedTemplateFactory parameterizedTemplateFactory,
            FintCache<String, Integer> publishedApplicationResourceHashCache
    ) {
       parameterizedTemplate = parameterizedTemplateFactory.createTemplate(ApplicationResource.class);
        this.publishedApplicationResourceHashCache = publishedApplicationResourceHashCache;
        entityTopicNameParameters = EntityTopicNameParameters
                .builder()
                .topicNamePrefixParameters(
                        TopicNamePrefixParameters.stepBuilder()
                                .orgIdApplicationDefault()
                                .domainContextApplicationDefault()
                                .build()
                )
                .resourceName("applicationresource")
                .build();
        entityTopicService.createOrModifyTopic(entityTopicNameParameters,
                EntityTopicConfiguration.stepBuilder()
                        .partitions(1)
                        .lastValueRetainedForever()
                        .nullValueRetentionTime(Duration.ofDays(7))
                        .cleanupFrequency(EntityCleanupFrequency.NORMAL)
                        .build()
                );
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
        log.info("Publishing application resource: {}", applicationResource);
        String key = applicationResource.getResourceId();
        parameterizedTemplate.send(
                ParameterizedProducerRecord.<ApplicationResource>builder()
                        .topicNameParameters(entityTopicNameParameters)
                        .key(key)
                        .value(applicationResource)
                        .build()
        );
    }
}
