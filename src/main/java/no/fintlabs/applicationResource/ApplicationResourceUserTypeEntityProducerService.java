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
public class ApplicationResourceUserTypeEntityProducerService {
    private final EntityProducer<ApplicationResourceUserType> entityProducer;
    private final EntityTopicNameParameters entityTopicNameParameters;

    private final FintCache<String, ApplicationResourceUserType> ApplicationResourceUserTypeCache;

    public ApplicationResourceUserTypeEntityProducerService(
            EntityProducerFactory entityProducerFactory,
            EntityTopicService entityTopicService,
            FintCache<String, ApplicationResourceUserType> ApplicationResourceUserTypeCache
    ) {
        entityProducer = entityProducerFactory.createProducer(ApplicationResourceUserType.class);
        this.ApplicationResourceUserTypeCache = ApplicationResourceUserTypeCache;
        entityTopicNameParameters = EntityTopicNameParameters
                .builder()
                .resource("applicationresource-usertype")
                .build();
        entityTopicService.ensureTopic(entityTopicNameParameters, 0);
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
        entityProducer.send(
                EntityProducerRecord.<ApplicationResourceUserType>builder()
                        .topicNameParameters(entityTopicNameParameters)
                        .key(key)
                        .value(applicationResourceUserType)
                        .build()
        );
    }
}
