package no.fintlabs.applicationResource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ApplicationResourceUserTypePublishingComponent {
    private final ApplicationResourceUserTypeService applicationResourceUserTypeService;
    private final ApplicationResourceUserTypeEntityProducerService applicationResourceUserTypeEntityProducerService;

    public ApplicationResourceUserTypePublishingComponent(ApplicationResourceUserTypeService applicationResourceUserTypeService, ApplicationResourceUserTypeEntityProducerService applicationResourceUserTypeEntityProducerService) {
        this.applicationResourceUserTypeService = applicationResourceUserTypeService;
        this.applicationResourceUserTypeEntityProducerService = applicationResourceUserTypeEntityProducerService;
    }

    @Scheduled(
            initialDelayString = "${fint.kontroll.resource.publishing.initial-delay}",
            fixedDelayString = "${fint.kontroll.resource.publishing.fixed-delay}"
    )
    public void publishApplicationResourceUserTypes() {

        List<ApplicationResourceUserType> applicationResourceUserTypes = applicationResourceUserTypeService.getAllApplicationResourceUserTypes();
        List<ApplicationResourceUserType> applicationResourceUserTypesPublished = applicationResourceUserTypeEntityProducerService
                .publish(applicationResourceUserTypes);
        log.info("ApplicationResourceUserTypes created/published to kafka: " + applicationResourceUserTypes.size() +"/" + applicationResourceUserTypesPublished.size());
    }
}
