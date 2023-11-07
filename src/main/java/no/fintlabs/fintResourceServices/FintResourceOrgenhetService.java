package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fintlabs.cache.FintCache;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FintResourceOrgenhetService {
    private final FintCache<String,OrganisasjonselementResource> organisasjonselementResourceFintCache;

    public FintResourceOrgenhetService(FintCache<String, OrganisasjonselementResource> organisasjonselementResourceFintCache) {
        this.organisasjonselementResourceFintCache = organisasjonselementResourceFintCache;
    }

    public String getOrgUnitName(Link link){
        String orgUnitHref = link.getHref();

        return organisasjonselementResourceFintCache
                .getOptional(orgUnitHref)
                .map(organisasjonselementResource -> organisasjonselementResource.getNavn())
                .orElse("");
    }

    public String getOrgUnitId(Link link){
        String orgUnitHref = link.getHref();

        return organisasjonselementResourceFintCache
                .getOptional(orgUnitHref)
                .map(organisasjonselementResource -> organisasjonselementResource.getOrganisasjonsId().getIdentifikatorverdi())
                .orElse("");
    }


}
