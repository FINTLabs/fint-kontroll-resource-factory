package no.fintlabs;

import no.fintlabs.kafka.entity.EntityConsumerFactoryService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConsumerConfiguration {
    private final EntityConsumerFactoryService entityConsumerFactoryService;

    public EntityConsumerConfiguration(EntityConsumerFactoryService entityConsumerFactoryService) {
        this.entityConsumerFactoryService = entityConsumerFactoryService;
    }




}
