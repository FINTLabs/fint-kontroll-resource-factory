package no.fintlabs.applicationResource;

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
public class ApplicationResourceUserTypeEntityProducerService {
    private final ParameterizedTemplate<ApplicationResourceUserType> parameterizedTemplate;
    private final EntityTopicNameParameters entityTopicNameParameters;
    private final FintCache<String, ApplicationResourceUserType> ApplicationResourceUserTypeCache;

    public ApplicationResourceUserTypeEntityProducerService(
            ParameterizedTemplateFactory parameterizedTemplateFactory,
            EntityTopicService entityTopicService,
            FintCache<String, ApplicationResourceUserType> ApplicationResourceUserTypeCache
    ) {
        parameterizedTemplate = parameterizedTemplateFactory.createTemplate(ApplicationResourceUserType.class);
        this.ApplicationResourceUserTypeCache = ApplicationResourceUserTypeCache;
        entityTopicNameParameters = EntityTopicNameParameters
                .builder()
                .topicNamePrefixParameters(
                        TopicNamePrefixParameters.stepBuilder()
                                .orgIdApplicationDefault()
                                .domainContextApplicationDefault()
                                .build()
                )
                .resourceName("applicationresource-usertype")
                .build();
        entityTopicService.createOrModifyTopic(
                entityTopicNameParameters,
                EntityTopicConfiguration.stepBuilder()
                        .partitions(1)
                        .lastValueRetainedForever()
                        .nullValueRetentionTime(Duration.ofDays(7))
                        .cleanupFrequency(EntityCleanupFrequency.NORMAL)
                        .build()
        );
    }


    public List<ApplicationResourceUserType> publish(List<ApplicationResourceUserType> applicationResourceUserTypes) {

        return applicationResourceUserTypes
                .stream()
                .filter(userType -> ApplicationResourceUserTypeCache
                        .getOptional(userType.internalUserType())
                        .map(publishedUserType -> !userType.equals(publishedUserType))
                        .orElse(true)
                )
                .peek(this::publish)
                .toList();
    }

    private void publish(ApplicationResourceUserType applicationResourceUserType) {
        String key = applicationResourceUserType.internalUserType();
        parameterizedTemplate.send(
                ParameterizedProducerRecord.<ApplicationResourceUserType>builder()
                        .topicNameParameters(entityTopicNameParameters)
                        .key(key)
                        .value(applicationResourceUserType)
                        .build()
        );
    }
}
