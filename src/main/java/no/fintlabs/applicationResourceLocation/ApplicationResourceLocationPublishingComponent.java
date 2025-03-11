package no.fintlabs.applicationResourceLocation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ApplicationResourceLocationPublishingComponent {
    private final ApplicationResourceLocationService ApplicationResourceLocationService;
    private final ApplicationResourceLocationEntityProducerService ApplicationResourceLocationEntityProducerService;

    public ApplicationResourceLocationPublishingComponent(ApplicationResourceLocationService ApplicationResourceLocationService, ApplicationResourceLocationEntityProducerService ApplicationResourceLocationEntityProducerService) {
        this.ApplicationResourceLocationService = ApplicationResourceLocationService;
        this.ApplicationResourceLocationEntityProducerService = ApplicationResourceLocationEntityProducerService;
    }

    @Scheduled(
            initialDelayString = "${fint.kontroll.resource.publishing.initial-delay-application-resource-location}",
            fixedDelayString = "${fint.kontroll.resource.publishing.fixed-delay}"
    )
    public void publishApplicationResourceLocations() {

        List<ApplicationResourceLocation> ApplicationResourceLocations = ApplicationResourceLocationService.getAllApplicationResourceLocations();
        List<ApplicationResourceLocation> ApplicationResourceLocationsPublished = ApplicationResourceLocationEntityProducerService
                .publish(ApplicationResourceLocations);
        log.info("ApplicationResourceLocations created/published to kafka: " + ApplicationResourceLocations.size() +"/" + ApplicationResourceLocationsPublished.size());
    }
}