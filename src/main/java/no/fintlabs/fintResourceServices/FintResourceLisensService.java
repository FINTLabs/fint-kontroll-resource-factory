package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import org.springframework.stereotype.Service;

import static no.fintlabs.links.ResourceLinkUtil.identifikatorNameToLowerCase;
import static no.fintlabs.links.ResourceLinkUtil.systemIdToLowerCase;

@Service
@Slf4j
public class FintResourceLisensService {
    private final FintCache<String, OrganisasjonselementResource> organisasjonselementResourcesFintCache;
    private final FintCache<String, LisensResource> lisensResourceFintCache;

    public FintResourceLisensService(
            FintCache<String, OrganisasjonselementResource> organisasjonselementResourcesFintCache, FintCache<String, LisensResource> lisensResourceFintCache) {
        this.organisasjonselementResourcesFintCache = organisasjonselementResourcesFintCache;
        this.lisensResourceFintCache = lisensResourceFintCache;
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

    public String getLisensResourceSystemId(Link link) {
        String lisensHref = identifikatorNameToLowerCase(link.getHref());
        return lisensResourceFintCache
                .getOptional(systemIdToLowerCase( lisensHref))
                .map(lisensResource -> lisensResource.getSystemId().getIdentifikatorverdi())
                .orElse("");
    }
    public String getLisensResourceName(Link link) {
        String lisensHref = identifikatorNameToLowerCase(link.getHref());
        return lisensResourceFintCache
                .getOptional(systemIdToLowerCase( lisensHref))
                .map(LisensResource::getLisensnavn)
                .orElse("");
    }

    public Long getResourceLimit(LisensResource lisensResource) {
        return (long) lisensResource.getLisensantall();
    }
}
