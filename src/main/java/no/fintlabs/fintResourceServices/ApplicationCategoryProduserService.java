package no.fintlabs.fintResourceServices;


import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;

@Service
@Slf4j
public class ApplicationCategoryProduserService {
    private final ParameterizedTemplate<String> parameterizedTemplate;
    private final EntityTopicNameParameters entityTopicNameParameters;


    public ApplicationCategoryProduserService(
            ParameterizedTemplateFactory parameterizedTemplateFactory,
            EntityTopicService entityTopicService
    ) {
        parameterizedTemplate = parameterizedTemplateFactory.createTemplate(String.class);
        entityTopicNameParameters = EntityTopicNameParameters
                .builder()
                .topicNamePrefixParameters(
                        TopicNamePrefixParameters.stepBuilder()
                                .orgIdApplicationDefault()
                                .domainContextApplicationDefault()
                                .build()
                )
                .resourceName("applicationcategory")
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

    public void publish(Map<String, String> appCategory) {
        appCategory.forEach((key, value) -> {
                    log.info("{} :: {}", key, value);
                    parameterizedTemplate.send(
                            ParameterizedProducerRecord.<String>builder()
                                    .topicNameParameters(entityTopicNameParameters)
                                    .key(key)
                                    .value(value)
                                    .build()

                    );
                }
        );
    }
}
