package no.fintlabs.fintResourceServices;


import lombok.extern.slf4j.Slf4j;
import no.fintlabs.kafka.entity.EntityProducer;
import no.fintlabs.kafka.entity.EntityProducerFactory;
import no.fintlabs.kafka.entity.EntityProducerRecord;
import no.fintlabs.kafka.entity.topic.EntityTopicNameParameters;
import no.fintlabs.kafka.entity.topic.EntityTopicService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ApplicationCategoryProduserService {
    private final EntityProducer entityProducer;
    private final EntityTopicNameParameters entityTopicNameParameters;


    public ApplicationCategoryProduserService(
            EntityProducerFactory entityProducerFactory,
            EntityTopicService entityTopicService) {

        entityProducer = entityProducerFactory.createProducer(String.class);
        entityTopicNameParameters = EntityTopicNameParameters
                .builder()
                .resource("applicationcategory")
                .build();
        entityTopicService.ensureTopic(entityTopicNameParameters,0);
    }

    public void publish(Map<String,String> appCategory){
        appCategory.forEach((key,value) -> {
            log.info("{} :: {}", key, value);
            entityProducer.send(
                    EntityProducerRecord.<String>builder()
                            .topicNameParameters(entityTopicNameParameters)
                            .key(key)
                            .value(value)
                            .build()

            );
        });
    };
}
