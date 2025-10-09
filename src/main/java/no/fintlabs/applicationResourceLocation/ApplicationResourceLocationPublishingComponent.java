package no.fintlabs.applicationResourceLocation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationResourceLocationPublishingComponent {
    private final ApplicationResourceLocationService applicationResourceLocationService;
    private final ApplicationResourceLocationEntityProducerService applicationResourceLocationEntityProducerService;


    @Scheduled(
            initialDelayString = "${fint.kontroll.resource.publishing.initial-delay-application-resource-location}",
            fixedDelayString = "${fint.kontroll.resource.publishing.fixed-delay}"
    )
    public void publishApplicationResourceLocations() {

        List<ApplicationResourceLocation> applicationResourceLocations = applicationResourceLocationService.getAllApplicationResourceLocations();
        List<ApplicationResourceLocation> applicationResourceLocationsPublished = applicationResourceLocationEntityProducerService
                .publish(applicationResourceLocations);
        log.info("ApplicationResourceLocations created/published to kafka: " + applicationResourceLocations.size() +"/" + applicationResourceLocationsPublished.size());
    }
}