package no.fintlabs.applicationResource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "fint.kontroll.resource")
public class ApplicationResourceConfiguration {
    private LicenseEnforcement licenseEnforcement;
    private ValidRolesForUsertype validRolesForUsertype;

    @Data
    public static class LicenseEnforcement {
        private List<String> hardStop;
        private List<String> floating;
        private List<String> freeAll;
        private List<String> freeEdu;
        private List<String> freeStudent;
    }
    @Data
    public static class ValidRolesForUsertype {
        private List<String> student;
        private List<String> employeeFaculty;
        private List<String> employeeStaff;
        private List<String> allTypes;
    }
}
