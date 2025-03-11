package no.fintlabs.applicationResourceLocation;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisenstilgangResource;
import no.fintlabs.fintResourceServices.FintResourceLisensService;
import no.fintlabs.fintResourceServices.FintResourceOrgenhetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class ApplicationResourceLocationTest {
    @Mock
    private FintCache<String, LisenstilgangResource> lisenstilgangResourceFintCache;
    @Mock
    private FintResourceOrgenhetService fintResourceOrgenhetService;
    @Mock
    private FintResourceLisensService fintResourceLisensService;

    @BeforeEach
    public void setUp() {
        lisenstilgangResourceFintCache = Mockito.mock(FintCache.class);
        fintResourceOrgenhetService = Mockito.mock(FintResourceOrgenhetService.class);
        fintResourceLisensService = Mockito.mock(FintResourceLisensService.class);
    }

    @Test
    void getAllApplicationResourceLocations() {
        Link lisensLink = new Link("https://example.com/lisens/systemid/m365");
        Link orgenhetLink = new Link("https://example.com/organisasjons/systemid/varfk");

        String m365_systemid ="m365";
        String m365_navn ="Microsoft 365";
        String varfk_systemid = "varfk";
        String varfk_navn ="Vår FK";

        LisenstilgangResource lisenstilgangResource = new LisenstilgangResource();
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi("m365_varfk");
        lisenstilgangResource.setSystemId(systemId);

        lisenstilgangResource.addLisens(lisensLink);
        lisenstilgangResource.addLisenskonsument(orgenhetLink);

        ApplicationResourceLocationService applicationResourceLocationService = new ApplicationResourceLocationService(
                lisenstilgangResourceFintCache,
                fintResourceOrgenhetService,
                fintResourceLisensService
        );

        given(lisenstilgangResourceFintCache.getAll()).willReturn(List.of(lisenstilgangResource));
        given(fintResourceLisensService.getLisensResourceSystemId(lisensLink)).willReturn(m365_systemid);
        given(fintResourceLisensService.getLisensResourceName(lisensLink)).willReturn(m365_navn);
        given(fintResourceOrgenhetService.getOrgUnitId(orgenhetLink)).willReturn(varfk_systemid);
        given(fintResourceOrgenhetService.getOrgUnitName(orgenhetLink)).willReturn(varfk_navn);

        List<ApplicationResourceLocation> applicationResourceLocations = applicationResourceLocationService.getAllApplicationResourceLocations();
        assertEquals(1, applicationResourceLocations.size());

        ApplicationResourceLocation applicationResourceLocation = applicationResourceLocations.getFirst();
        assertEquals(m365_systemid, applicationResourceLocation.getResourceId());
        assertEquals(m365_navn, applicationResourceLocation.getResourceName());
        assertEquals(varfk_systemid, applicationResourceLocation.getOrgUnitId());
        assertEquals(varfk_navn, applicationResourceLocation.getOrgUnitName());
    }
}
