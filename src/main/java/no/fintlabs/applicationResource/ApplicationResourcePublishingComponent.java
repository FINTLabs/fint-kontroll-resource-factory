package no.fintlabs.applicationResource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ApplicationResourcePublishingComponent {
    private final ApplicationResourceService applicationResourceService;
    private final ApplicationResourceEntityProducerService applicationResourceEntityProducerService;

    public ApplicationResourcePublishingComponent(ApplicationResourceService applicationResourceService, ApplicationResourceEntityProducerService applicationResourceEntityProducerService) {
        this.applicationResourceService = applicationResourceService;
        this.applicationResourceEntityProducerService = applicationResourceEntityProducerService;
    }

    @Scheduled(
            initialDelayString = "${fint.kontroll.resource.publishing.initial-delay}",
            fixedDelayString = "${fint.kontroll.resource.publishing.fixed-delay}"
    )
    public void publishApplicationResources() {

        List<ApplicationResource> applicationResources = applicationResourceService.getAllApplicationResources();
        List<ApplicationResource> applicationResourcesPublished = applicationResourceEntityProducerService
                .publish(applicationResources);
        log.info("ApplicationResources created/published to kafka: " + applicationResources.size() +"/" + applicationResourcesPublished.size());
    }
}
