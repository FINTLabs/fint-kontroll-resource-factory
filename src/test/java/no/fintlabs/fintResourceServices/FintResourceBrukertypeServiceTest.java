package no.fintlabs.fintResourceServices;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FintResourceBrukertypeServiceTest {
    @Mock
    private FintCache<String, BrukertypeResource> brukertypeResourceFintCache;

    private FintResourceBrukertypeService fintResourceBrukertypeService;

    @BeforeEach
    public void setUp() {
        fintResourceBrukertypeService = new FintResourceBrukertypeService(brukertypeResourceFintCache);

    }
    @Test
    void getAvailableForUsertypeIdsShouldReturnEmptyListWhenTilgjengeligforbrukertypeIsEmpty() {
        LisensResource lisensResource = new LisensResource();
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi("systemId1");
        lisensResource.setSystemId(systemId);

        List<String> userTypeIds = fintResourceBrukertypeService.getAvailableForUsertypeIds(lisensResource);
        assertTrue(userTypeIds.isEmpty());
    }

    @Test
    void getAvailableForUsertypeIdsShouldReturnElementsWhenTilgjengeligforbrukertypeIsNonEmpty() {
        LisensResource lisensResource = new LisensResource();
        Identifikator systemId = new Identifikator();
        systemId.setIdentifikatorverdi("systemId1");
        lisensResource.setSystemId(systemId);

        lisensResource.addLisenstilgang(new Link("https://example.com/lisenstilgang/1"));
        Link brukertype1 = new Link("https://example.com/brukertyper/1");
        Link brukertype2 = new Link("https://example.com/brukertyper/2");

        lisensResource.addTilgjengeligforbrukertype(brukertype1);
        lisensResource.addTilgjengeligforbrukertype(brukertype2);

        List<String> userTypeIds = fintResourceBrukertypeService.getAvailableForUsertypeIds(lisensResource);
        assertEquals(2, userTypeIds.size());
        assertEquals(Set.of("1", "2"), Set.copyOf(userTypeIds));

    }
}