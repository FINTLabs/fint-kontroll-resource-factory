package no.fintlabs;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintLinks;
import no.fintlabs.applicationResource.ApplicationResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fintlabs.applicationResource.ApplicationResourceUserType;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.ApplikasjonResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisenstilgangResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.ApplikasjonskategoriResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.LisensmodellResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.PlattformResource;
import no.fintlabs.links.ResourceLinkUtil;
import no.novari.kafka.consuming.*;
import no.novari.kafka.topic.name.EntityTopicNameParameters;
import no.novari.kafka.topic.name.TopicNamePrefixParameters;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
@Slf4j
public class EntityConsumerConfiguration {
    private final ParameterizedListenerContainerFactoryService listenerContainerFactoryService;
    private final ErrorHandlerFactory errorHandlerFactory;

    public EntityConsumerConfiguration(
            ParameterizedListenerContainerFactoryService listenerContainerFactoryService,
            ErrorHandlerFactory errorHandlerFactory
    ) {
        this.listenerContainerFactoryService = listenerContainerFactoryService;
        this.errorHandlerFactory = errorHandlerFactory;
    }

    private <T extends FintLinks> ConcurrentMessageListenerContainer<String, T> createCacheConsumer(
            String resourceReference,
            Class<T> resourceClass,
            FintCache<String, T> cache
    ) {

        return createRecordListenerFactory(
                resourceClass,
                consumerRecord -> cache.put(
                        ResourceLinkUtil.getSelfLinks(consumerRecord.value()),
                        consumerRecord.value()
                )
        ).createContainer(topic(resourceReference));
    }


    private <T> ParameterizedListenerContainerFactory<T> createRecordListenerFactory(
            Class<T> resourceClass,
            java.util.function.Consumer<ConsumerRecord<String, T>> recordProcessor
    ) {
        return listenerContainerFactoryService.createRecordListenerContainerFactory(
                resourceClass,
                recordProcessor,
                ListenerConfiguration.stepBuilder()
                        .groupIdApplicationDefault()
                        .maxPollRecordsKafkaDefault()
                        .maxPollIntervalKafkaDefault()
                        .seekToBeginningOnAssignment()
                        .build(),
                errorHandlerFactory.createErrorHandler(
                        ErrorHandlerConfiguration.<T>stepBuilder()
                                .noRetries()
                                .skipFailedRecords()
                                .build()
                )
        );
    }

    private EntityTopicNameParameters topic(String resourceName) {
        return EntityTopicNameParameters.builder()
                .topicNamePrefixParameters(
                        TopicNamePrefixParameters.stepBuilder()
                                .orgIdApplicationDefault()
                                .domainContextApplicationDefault()
                                .build()
                )
                .resourceName(resourceName)
                .build();
    }


    @Bean
    ConcurrentMessageListenerContainer<String, LisensResource> lisensResourceConcurrentMessageListenerContainer(
            FintCache<String, LisensResource> lisensResourceFintCache) {
        return createCacheConsumer(
                "eiendeler.applikasjon.lisens",
                LisensResource.class,
                lisensResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, LisenstilgangResource> lisenstilgangResourceConcurrentMessageListenerContainer(
            FintCache<String, LisenstilgangResource> lisenstilgangResourceFintCache) {
        return createCacheConsumer(
                "eiendeler.applikasjon.lisenstilgang",
                LisenstilgangResource.class,
                lisenstilgangResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ApplikasjonResource> applikasjonResourceConcurrentMessageListenerContainer(
            FintCache<String, ApplikasjonResource> applikasjonResourceFintCache) {
        return createCacheConsumer(
                "eiendeler.applikasjon.applikasjon",
                ApplikasjonResource.class,
                applikasjonResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, OrganisasjonselementResource> organisasjonselementResourceEntityConsumer(
            FintCache<String, OrganisasjonselementResource> organisasjonselementResourceCache) {
        return createCacheConsumer(
                "administrasjon.organisasjon.organisasjonselement",
                OrganisasjonselementResource.class,
                organisasjonselementResourceCache
        );
    }


    @Bean
    ConcurrentMessageListenerContainer<String, BrukertypeResource> brukertypeResourceConcurrentMessageListenerContainer(
            FintCache<String, BrukertypeResource> brukertypeResourceFintCache) {
        return createCacheConsumer(
                "eiendeler.kodeverk.brukertype",
                BrukertypeResource.class,
                brukertypeResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, LisensmodellResource> lisensmodellResourceConcurrentMessageListenerContainer(
            FintCache<String, LisensmodellResource> lisensmodellResourceFintCache) {
        return createCacheConsumer(
                "eiendeler.kodeverk.lisensmodell",
                LisensmodellResource.class,
                lisensmodellResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, PlattformResource> plattformResourceConcurrentMessageListenerContainer(
            FintCache<String, PlattformResource> plattformResourceFintCache) {
        return createCacheConsumer(
                "eiendeler.kodeverk.plattform",
                PlattformResource.class,
                plattformResourceFintCache
        );
    }
    @Bean
    ConcurrentMessageListenerContainer<String, ApplikasjonskategoriResource> applikasjonskategoriResourceConcurrentMessageListenerContainer(
            FintCache<String, ApplikasjonskategoriResource> applikasjonskategoriformResourceFintCache) {
        return createCacheConsumer(
                "eiendeler.kodeverk.applikasjonskategori",
                ApplikasjonskategoriResource.class,
                applikasjonskategoriformResourceFintCache
        );
    }

    @Bean
    ConcurrentMessageListenerContainer<String, ApplicationResource> applicationResourceEntityConsumer(
            FintCache<String, Integer> publishedApplicationResourceHashCache) {


        return createRecordListenerFactory(
                ApplicationResource.class,
                consumerRecord ->
                        publishedApplicationResourceHashCache.put(
                                consumerRecord.value().getResourceId(),
                                consumerRecord.value().hashCode()
                        )
        ).createContainer(topic("applicationresource"));
    }
    @Bean
    ConcurrentMessageListenerContainer<String, ApplicationResourceUserType> applicationResourceUserTypeEntityConsumer(
            FintCache<String, ApplicationResourceUserType> publishedApplicationResourceUserTypeHashCache) {
        return createRecordListenerFactory(
                ApplicationResourceUserType.class,
                consumerRecord ->
                        publishedApplicationResourceUserTypeHashCache.put(
                                consumerRecord.value().internalUserType(),
                                consumerRecord.value()
                        )
        ).createContainer(topic("applicationresource-usertype"));
    }
}
