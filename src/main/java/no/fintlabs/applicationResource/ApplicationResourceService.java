package no.fintlabs.applicationResource;

import no.fint.model.resource.Link;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocationService;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceServices.*;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.cache.FintCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApplicationResourceService {
    private final FintCache<String, LisensResource> lisensResourceFintCache;
    private final FintResourceLisensService fintResourceLisensService;
    private final FintResourcePlattformService fintResourcePlattformService;
    private final FintResourceBrukertypeService fintResourceBrukertypeService;
    private final FintResourceLisensmodelService fintResourceLisensmodelService;
    private final FintResourceApplikasjonsKategoriService fintResourceApplikasjonsKategoriService;
    private final ApplicationResourceLocationService applicationResourceLocationService;
    private final ApplicationResourceEntityProducerService applicationResourceEntityProducerService;

    @Value("${fint.kontroll.resource.license-enforcement.hard-stop}")
    private List<String> hardStopLicenseModels;
    @Value("${fint.kontroll.resource.license-enforcement.floating}")
    private List<String> floatingLicenseModels;
    @Value("${fint.kontroll.resource.license-enforcement.free-all}")
    private List<String> freeAllLicenseModels;
    @Value("${fint.kontroll.resource.license-enforcement.free-edu}")
    private List<String> freeEduLicenseModels;
    @Value("${fint.kontroll.resource.license-enforcement.free-student}")
    private List<String> freeStudentLicenseModels;
    @Value("${fint.kontroll.resource.valid-roles-for-usertype.student}")
    private String studentRole;
    @Value("${fint.kontroll.resource.valid-roles-for-usertype.employee-faculty}")
    private String employeeFacultyRole;
    @Value("${fint.kontroll.resource.valid-roles-for-usertype.employee-staff}")
    private String employeeStaffRole;

    public ApplicationResourceService(FintCache<String, LisensResource> lisensResourceFintCache,
                                      FintResourceLisensService fintResourceLisensService,
                                      FintResourcePlattformService fintResourcePlattformService,
                                      FintResourceBrukertypeService fintResourceBrukertypeService,
                                      FintResourceLisensmodelService fintResourceLisensmodelService,
                                      ApplicationResourceLocationService applicationResourceLocationService,
                                      ApplicationResourceEntityProducerService applicationResourceEntityProducerService,
                                      FintResourceApplikasjonsKategoriService fintResourceApplikasjonsKategoriService) {
        this.lisensResourceFintCache = lisensResourceFintCache;
        this.fintResourceLisensService = fintResourceLisensService;
        this.fintResourcePlattformService = fintResourcePlattformService;
        this.fintResourceBrukertypeService = fintResourceBrukertypeService;
        this.fintResourceLisensmodelService = fintResourceLisensmodelService;
        this.applicationResourceLocationService = applicationResourceLocationService;
        this.applicationResourceEntityProducerService = applicationResourceEntityProducerService;
        this.fintResourceApplikasjonsKategoriService = fintResourceApplikasjonsKategoriService;
    }

    @Scheduled(
            initialDelayString = "${fint.kontroll.user.publishing.initial-delay}",
            fixedDelayString = "${fint.kontroll.user.publishing.fixed-delay}"
    )
    public void toApplicationResourceFromFintObject() {
        List<ApplicationResource> applicationResources = lisensResourceFintCache.getAllDistinct()
                .stream()
                .filter(lisensResource -> !lisensResource.getLisenstilgang().isEmpty())
                .filter(lisensResource -> !lisensResource.getApplikasjon().isEmpty())
                .map(this::createApplicationResource)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(applicationResource -> ! applicationResource.getApplicationCategory().contains("Pedagogisk verktøy"))
                .toList();

        List<ApplicationResource> applicationResourcesPublished = applicationResourceEntityProducerService
                .publish(applicationResources);
        log.info("ApplicationResources created/published to kafka: " + applicationResources.size() +"/" + applicationResourcesPublished.size());
    }


    private Optional<ApplicationResource> createApplicationResource(LisensResource lisensResource) {

        ApplicationResource applicationResource = new ApplicationResource();

        applicationResource.setResourceId(lisensResource.getSystemId().getIdentifikatorverdi());
        applicationResource.setResourceName(lisensResource.getLisensnavn());
        applicationResource.setResourceType("ApplicationResource");
        applicationResource.setResourceOwnerOrgUnitId(fintResourceLisensService.getResourceOwnerOrgUnitId(lisensResource));
        applicationResource.setResourceOwnerOrgUnitName(fintResourceLisensService.getResourceOwnerOrgUnitName(lisensResource));
        applicationResource.setResourceLimit(fintResourceLisensService.getResourceLimit(lisensResource));
        //applicationResource.setPlatform(fintResourcePlattformService.getPlatform(lisensResource));
        applicationResource.setAccessType(fintResourceLisensmodelService.getAccessType(lisensResource));
        applicationResource.setValidForRoles(fintResourceBrukertypeService.getValidForRoleNames(lisensResource));
        applicationResource.setUserTypes(mapValidForRolesToUserTypes(fintResourceBrukertypeService.getAvailableForUsertypeIds(lisensResource)));
        applicationResource.setValidForOrgUnits(applicationResourceLocationService.getValidForOrgunits(lisensResource));
        applicationResource.setApplicationCategory(fintResourceApplikasjonsKategoriService.getApplikasjonskategori(lisensResource));
        // Nye felter 3.18
        //applicationResource.setLicenseModel(fintResourceLisensmodelService.getLicenseModel(lisensResource));
        applicationResource.setLicenseEnforcement(mapLicenseModelToLicenseEnforcement(lisensResource));
        applicationResource.setStatus("ACTIVE");
//        applicationResource.setStatusChanged();
        return Optional.of(applicationResource);
    }
    private String mapLicenseModelToLicenseEnforcement(LisensResource lisensResource) {
        Optional<String> licenseModel =
                lisensResource.getTilgjengeligforbrukertype()
                        .stream()
                        .findFirst()
                        .map(Link::getHref);

        if (licenseModel.isEmpty()) {
            return "NOT-SPECIFIED";
        }
        String licenseModelId = StringUtils.substringAfterLast(licenseModel.get(), "/");

        if (hardStopLicenseModels.contains(licenseModelId)) {
            return "HARD-STOP";
        }
        if (floatingLicenseModels.contains(licenseModelId)) {
            return "FLOATING";
        }
        if (freeStudentLicenseModels.contains(licenseModelId)) {
            return "FREE-STUDENT";
        }
        if (freeEduLicenseModels.contains(licenseModelId)) {
            return "FREE-EDU";
        }
        if (freeAllLicenseModels.contains(licenseModelId)) {
            return "FREE-ALL";
        }
        return "FREE-ALL";
    }
    private List<String> mapValidForRolesToUserTypes(List<String> validForRoles) {
        List<String> userTypes = new ArrayList<>();

        if (validForRoles.contains(studentRole)) {
            userTypes.add("STUDENT");
        }
        if (validForRoles.contains(employeeFacultyRole)) {
            userTypes.add("EMPLOYEEFACULTY");
        }
        if (validForRoles.contains(employeeStaffRole)) {
            userTypes.add("EMPLOYEESTAFF");
        }
        if (validForRoles.contains(studentRole) && validForRoles.contains(employeeFacultyRole)) {
            userTypes.add("EDU");
        }
        if (validForRoles.contains(employeeFacultyRole) && validForRoles.contains(employeeStaffRole)) {
            userTypes.add("EMPLOYEE");
        }
        if (validForRoles.contains(studentRole)
                && validForRoles.contains(employeeFacultyRole)
                && validForRoles.contains(employeeStaffRole)
        ) {
            userTypes.add("ALLTYPES");
        }
        return userTypes;
    }
}
