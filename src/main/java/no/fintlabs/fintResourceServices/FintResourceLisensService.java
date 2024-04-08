package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FintResourceLisensService {
    private final FintCache<String, OrganisasjonselementResource> organisasjonselementResourcesFintCache;

    public FintResourceLisensService(
            FintCache<String, OrganisasjonselementResource> organisasjonselementResourcesFintCache) {
        this.organisasjonselementResourcesFintCache = organisasjonselementResourcesFintCache;
    }

    public String getResourceOwnerOrgUnitName(LisensResource lisensResource) {
        if (lisensResource.getLisenseier().isEmpty()) {return " ingen lisenseierinfo";}
        String lisenseierHref = lisensResource.getLisenseier().get(0).getHref().toLowerCase();

        return organisasjonselementResourcesFintCache
                .getOptional(lisenseierHref)
                .map(OrganisasjonselementResource::getNavn)
                .orElse(" ");
    }

    public String getResourceOwnerOrgUnitId(LisensResource lisensResource) {
        if (lisensResource.getLisenseier().isEmpty()) {return " ingen lisenseierinfo";}
        String lisensEierHref = lisensResource.getLisenseier().get(0).getHref().toLowerCase();

        return organisasjonselementResourcesFintCache
                .getOptional(lisensEierHref)
                .map(orgunit -> orgunit.getOrganisasjonsId().getIdentifikatorverdi())
                .orElse("ingen lisenseier funnet ");
    }

    public Long getResourceLimit(LisensResource lisensResource) {
        return (long) lisensResource.getLisensantall();
    }
}
