package no.fintlabs.applicationResourceLocation;

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

@Service
public class ApplicationResourceLocationEntityProducerService {
   private final ParameterizedTemplate<ApplicationResourceLocation> parameterizedTemplate;
    private final EntityTopicNameParameters entityTopicNameParameters;
    private final FintCache<String, ApplicationResourceLocation> applicationResourceLocationCache;

    public ApplicationResourceLocationEntityProducerService(
            ParameterizedTemplateFactory parameterizedTemplateFactory,
            EntityTopicService entityTopicService,
            FintCache<String, ApplicationResourceLocation> applicationResourceLocationCache
    ) {
        parameterizedTemplate = parameterizedTemplateFactory.createTemplate(ApplicationResourceLocation.class);
        this.applicationResourceLocationCache = applicationResourceLocationCache;
        entityTopicNameParameters = EntityTopicNameParameters
                .builder()
                .topicNamePrefixParameters(
                        TopicNamePrefixParameters
                                .stepBuilder()
                                .orgIdApplicationDefault()
                                .domainContextApplicationDefault()
                                .build()
                )
                .resourceName("applicationresource-location")
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
        parameterizedTemplate.send(
                ParameterizedProducerRecord.<ApplicationResourceLocation>builder()
                        .topicNameParameters(entityTopicNameParameters)
                        .key(key)
                        .value(applicationResourceLocation)
                        .build()
        );
        applicationResourceLocationCache.put(key, applicationResourceLocation);
    }
}

