package no.fintlabs.applicationResource;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import no.fintlabs.fintResourceServices.FintResourceBrukertypeService;
import no.fintlabs.kodeverk.Brukertype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@Import({ApplicationResourceConfiguration.class,
        ApplicationResourceConfiguration.LicenseEnforcement.class,
        ApplicationResourceConfiguration.ValidRolesForUsertype.class
})
class ApplicationResourceUserTypeServiceTest {
    private ApplicationResourceConfiguration applicationResourceConfiguration;
    @Mock
    private FintResourceBrukertypeService fintResourceBrukertypeService;
    @BeforeEach
    public void setUp() {
        applicationResourceConfiguration = new ApplicationResourceConfiguration() {{
            setLicenseEnforcement(new ApplicationResourceConfiguration.LicenseEnforcement() {{
                setHardStop(List.of("1"));
            }});
            setValidRolesForUsertype(new ApplicationResourceConfiguration.ValidRolesForUsertype() {{
                setStudent(List.of("1", "4"));
                setEmployeeFaculty(List.of("2", "4", "5"));
                setEmployeeStaff(List.of("3", "5"));
                setAllTypes(List.of("4"));
            }});
        }};
    }
    @Test
    void givenAllBrukertypeResourcesShouldReturnAllApplicationResourceUserTypes() {
        BrukertypeResource brukertypeResourceElev = new BrukertypeResource();
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi("1");
        brukertypeResourceElev.setSystemId(systemId);
        brukertypeResourceElev.setNavn("Elev");

        BrukertypeResource brukertypeResourceAnsattSkole = new BrukertypeResource();
        Identifikator systemId2 = new Identifikator();
        systemId2.setIdentifikatorverdi("2");
        brukertypeResourceAnsattSkole.setSystemId(systemId2);
        brukertypeResourceAnsattSkole.setNavn("Ansatt skole");

        BrukertypeResource brukertypeResourceAnsattUtenomSkole = new BrukertypeResource();
        Identifikator systemId3 = new Identifikator();
        systemId3.setIdentifikatorverdi("3");
        brukertypeResourceAnsattUtenomSkole.setSystemId(systemId3);
        brukertypeResourceAnsattUtenomSkole.setNavn("Ansatt utenom skole");

        BrukertypeResource brukertypeResourceAlleBrukere = new BrukertypeResource();
        Identifikator systemId4 = new Identifikator();
        systemId4.setIdentifikatorverdi("4");
        brukertypeResourceAlleBrukere.setSystemId(systemId4);
        brukertypeResourceAlleBrukere.setNavn("Alle brukere");

        ApplicationResourceUserType applicationResourceUserTypeElev =
                new ApplicationResourceUserType(Brukertype.STUDENT.name(), "Elev");
        ApplicationResourceUserType applicationResourceUserTypeAnsattSkole =
                new ApplicationResourceUserType(Brukertype.EMPLOYEEFACULTY.name(), "Ansatt skole");
        ApplicationResourceUserType applicationResourceUserTypeAnsattUtenomSkole =
                new ApplicationResourceUserType(Brukertype.EMPLOYEESTAFF.name(), "Ansatt utenom skole");
        ApplicationResourceUserType applicationResourceUserTypeAlleBrukere =
                new ApplicationResourceUserType(Brukertype.ALLTYPES.name(), "Alle brukere");

        fintResourceBrukertypeService = Mockito.mock(FintResourceBrukertypeService.class);

        ApplicationResourceUserTypeService applicationResourceUserTypeService =
                new ApplicationResourceUserTypeService(applicationResourceConfiguration, fintResourceBrukertypeService);

        given(fintResourceBrukertypeService.getAllBrukertypeResources())
                .willReturn(Optional.of(List.of(
                        brukertypeResourceElev,
                        brukertypeResourceAnsattSkole,
                        brukertypeResourceAnsattUtenomSkole,
                        brukertypeResourceAlleBrukere)
                ));

        Set<ApplicationResourceUserType> applicationResourceUserTypes = new HashSet<>(applicationResourceUserTypeService.getAllApplicationResourceUserTypes());
        assertNotNull(applicationResourceUserTypes);
        assertEquals(4, applicationResourceUserTypes.size());
        assertEquals(Set.of(
                    applicationResourceUserTypeElev,
                    applicationResourceUserTypeAnsattSkole,
                    applicationResourceUserTypeAnsattUtenomSkole,
                    applicationResourceUserTypeAlleBrukere
                ),
                applicationResourceUserTypes);
    }
}