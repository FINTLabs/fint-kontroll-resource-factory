package no.fintlabs.applicationResource;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocation;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocationId;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocationService;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceServices.FintResourceApplikasjonsKategoriService;
import no.fintlabs.fintResourceServices.FintResourceBrukertypeService;
import no.fintlabs.fintResourceServices.FintResourceLisensService;
import no.fintlabs.fintResourceServices.FintResourceLisensmodelService;
import no.fintlabs.kodeverk.Brukertype;
import no.fintlabs.kodeverk.Handhevingstype;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Import;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@Import({ApplicationResourceConfiguration.class,
        ApplicationResourceConfiguration.LicenseEnforcement.class,
        ApplicationResourceConfiguration.ValidRolesForUsertype.class
})
class ApplicationResourceServiceTest {
    private ApplicationResourceService applicationResourceService;

    private ApplicationResourceConfiguration applicationResourceConfiguration;
    @Mock
    private FintResourceLisensService fintResourceLisensService;
    @Mock
    private FintResourceLisensmodelService fintResourceLisensmodelService;
    @Mock
    private FintResourceBrukertypeService fintResourceBrukertypeService;
    @Mock
    private ApplicationResourceLocationService applicationResourceLocationService;
    @Mock
    private FintResourceApplikasjonsKategoriService fintResourceApplikasjonsKategoriService;
    @Mock
    private FintCache<String, LisensResource> lisensResourceFintCache;

    @BeforeEach
    public void setUp() {
        applicationResourceConfiguration = new ApplicationResourceConfiguration(){{
            setLicenseEnforcement(new ApplicationResourceConfiguration.LicenseEnforcement(){{
                setHardStop(List.of("1"));
            }});
            setValidRolesForUsertype(new ApplicationResourceConfiguration.ValidRolesForUsertype(){{
                setStudent(List.of("1","4"));
                setEmployeeFaculty(List.of("2","4","5"));
                setEmployeeStaff(List.of("3","5"));
            }});
        }};
        lisensResourceFintCache = Mockito.mock(FintCache.class);
        fintResourceLisensService = Mockito.mock(FintResourceLisensService.class);
        fintResourceBrukertypeService = Mockito.mock(FintResourceBrukertypeService.class);
        fintResourceLisensmodelService = Mockito.mock(FintResourceLisensmodelService.class);
        applicationResourceLocationService = Mockito.mock(ApplicationResourceLocationService.class);
        fintResourceApplikasjonsKategoriService = Mockito.mock(FintResourceApplikasjonsKategoriService.class);
    }

    @Test
    void getAllApplicationResources() {
        LisensResource lisensResource = new LisensResource();
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi("systemId1");
        lisensResource.setSystemId(systemId);
        lisensResource.setLisensnavn("Adobe k12 User");

        Link brukertype1 = new Link("https://example.com/brukertyper/1");
        Link brukertype2 = new Link("https://example.com/brukertyper/2");

        lisensResource.addTilgjengeligforbrukertype(brukertype1);
        lisensResource.addTilgjengeligforbrukertype(brukertype2);
        lisensResource.addApplikasjon(new Link("https://example.com/applikasjoner/1"));
        lisensResource.addLisenstilgang(new Link("https://example.com/lisenstilgang/1"));
        lisensResource.addLisenseier(new Link("https://example.com/organisasjonselement/1"));

        List<ApplicationResourceLocation> applicationResourceLocations = List.of(
                ApplicationResourceLocation.builder()
                        .id(new ApplicationResourceLocationId(null, "1"))
                        .orgUnitName("Vår fylkeskommune")
                        .resourceLimit(100L)
                        .build()
        );
        applicationResourceService = new ApplicationResourceService(
                applicationResourceConfiguration,
                lisensResourceFintCache,
                fintResourceLisensService,
                null,
                fintResourceBrukertypeService,
                fintResourceLisensmodelService,
                applicationResourceLocationService,
                null,
                fintResourceApplikasjonsKategoriService
        );
        given(lisensResourceFintCache.getAllDistinct()).willReturn(List.of(lisensResource));
        given(fintResourceLisensService.getResourceOwnerOrgUnitId(lisensResource)).willReturn("varfk");
        given(fintResourceLisensService.getResourceOwnerOrgUnitName(lisensResource)).willReturn("Vår fylkeskommune");
        given(fintResourceLisensService.getResourceLimit(lisensResource)).willReturn(100L);
        given(fintResourceBrukertypeService.getValidForRoleNames(lisensResource)).willReturn(List.of("STUDENT"));
        given(fintResourceBrukertypeService.getAvailableForUsertypeIds(lisensResource)).willReturn(List.of("4"));
        given(fintResourceLisensmodelService.getAccessType(lisensResource)).willReturn("HARDSTOP");
        //given(applicationResourceLocationService.getValidForOrgunits(lisensResource)).willReturn(applicationResourceLocations);
        given((fintResourceApplikasjonsKategoriService.getApplikasjonskategori(lisensResource))).willReturn(List.of("Pedagogisk verktøy"));

        List<ApplicationResource> applicationResources = applicationResourceService.getAllApplicationResources();

        assertEquals(1, applicationResources.size());
        assertEquals(Handhevingstype.HARDSTOP.toString(), applicationResources.getFirst().getAccessType());
        assertEquals(Set.of(Brukertype.STUDENT.toString(), Brukertype.EMPLOYEEFACULTY.toString()),
                Set.copyOf(applicationResources.getFirst().getValidForRoles()));

    }
}