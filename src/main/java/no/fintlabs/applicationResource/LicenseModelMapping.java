package no.fintlabs.applicationResource;

import no.fint.model.resource.Link;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.kodeverk.Handhevingstype;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class LicenseModelMapping {

    public static String mapLicenseModelToLicenseEnforcement(
            LisensResource lisensResource,
            ApplicationResourceConfiguration applicationResourceConfiguration
    ){
        Optional<String> licenseModel =
                lisensResource.getLisenstilgang()
                        .stream()
                        .findFirst()
                        .map(Link::getHref);

        if (licenseModel.isEmpty()) {
            return Handhevingstype.NOTSPECIFIED.name();
        }
        String licenseModelId = StringUtils.substringAfterLast(licenseModel.get(), "/");

        if (applicationResourceConfiguration.getLicenseEnforcement().getHardStop().contains(licenseModelId)) {
            return Handhevingstype.HARDSTOP.name();
        }
        if (applicationResourceConfiguration.getLicenseEnforcement().getFloating().contains(licenseModelId)) {
            return Handhevingstype.FLOATING.name();
        }
        if (applicationResourceConfiguration.getLicenseEnforcement().getFreeStudent().contains(licenseModelId)) {
            return Handhevingstype.FREESTUDENT.name();
        }
        if (applicationResourceConfiguration.getLicenseEnforcement().getFreeEdu().contains(licenseModelId)) {
            return Handhevingstype.FREEEDU.name();
        }
        if (applicationResourceConfiguration.getLicenseEnforcement().getFreeAll() .contains(licenseModelId)) {
            return Handhevingstype.FREEALL.name();
        }
        return Handhevingstype.FREEALL.name();
    }
}
