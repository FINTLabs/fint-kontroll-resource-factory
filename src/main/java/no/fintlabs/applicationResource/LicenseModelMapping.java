package no.fintlabs.applicationResource;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.kodeverk.Handhevingstype;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LicenseModelMapping {

    public static String mapLicenseModelToLicenseEnforcement(
            LisensResource lisensResource,
            ApplicationResourceConfiguration applicationResourceConfiguration
    ){
        log.debug("Mapping license model to license enforcement for lisensResource ({}): {}",
                lisensResource.getSystemId().getIdentifikatorverdi(),
                lisensResource.getLisensnavn()
        );
        Optional<String> licenseModel =
                lisensResource.getLisensmodell()
                        .stream()
                        .findFirst()
                        .map(Link::getHref);

        if (licenseModel.isEmpty()) {
            log.warn("No license model found for lisensResource with systemId: {}",
                    lisensResource.getSystemId().getIdentifikatorverdi()
            );
            return Handhevingstype.NOTSET.name();
        }
        log.debug("License model found: {}", licenseModel.get());
        String licenseModelId = StringUtils.substringAfterLast(licenseModel.get(), "/");

        if (applicationResourceConfiguration.getLicenseEnforcement().getHardStop().contains(licenseModelId)) {
            log.debug("License model {} mapped to HARDSTOP", licenseModelId);
            return Handhevingstype.HARDSTOP.name();
        }
        if (applicationResourceConfiguration.getLicenseEnforcement().getFloating().contains(licenseModelId)) {
            log.debug("License model {} mapped to FLOATING", licenseModelId);
            return Handhevingstype.FLOATING.name();
        }
        if (applicationResourceConfiguration.getLicenseEnforcement().getFreeStudent().contains(licenseModelId)) {
            log.debug("License model {} mapped to FREESTUDENT", licenseModelId);
            return Handhevingstype.FREESTUDENT.name();
        }
        if (applicationResourceConfiguration.getLicenseEnforcement().getFreeEdu().contains(licenseModelId)) {
            log.debug("License model {} mapped to FREEEDU", licenseModelId);
            return Handhevingstype.FREEEDU.name();
        }
        if (applicationResourceConfiguration.getLicenseEnforcement().getFreeAll() .contains(licenseModelId)) {
            log.debug("License model {} mapped to FREEALL", licenseModelId);
            return Handhevingstype.FREEALL.name();
        }
        log.warn("No license enforcement found for license model {}. license enforcement set to FREEALL",
                licenseModelId
        );
        return Handhevingstype.FREEALL.name();
    }
}
