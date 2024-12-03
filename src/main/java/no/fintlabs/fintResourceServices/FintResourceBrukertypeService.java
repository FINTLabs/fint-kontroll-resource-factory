package no.fintlabs.fintResourceServices;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import no.fintlabs.links.ResourceLinkUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class FintResourceBrukertypeService {
    private final FintCache<String, BrukertypeResource> brukertypeResourceFintCache;

    public FintResourceBrukertypeService(FintCache<String, BrukertypeResource> brukertypeResourceFintCache) {
        this.brukertypeResourceFintCache = brukertypeResourceFintCache;
    }

    public List<String> getValidForRoleNames(LisensResource lisensResource) {

        if (lisensResource.getTilgjengeligforbrukertype().isEmpty()) {
            List<String> list = new ArrayList<>();
            list.add("Brukertype ikke satt");
            return list;
        }
        return lisensResource.getTilgjengeligforbrukertype()
                .stream()
                .map(Link::getHref)
                .map(ResourceLinkUtil::systemIdToLowerCase)
                .map(brukertypeResourceFintCache::getOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(BrukertypeResource::getNavn)
                .toList();
    }

    public List<String> getAvailableForUsertypeIds(LisensResource lisensResource) {

        if (lisensResource.getTilgjengeligforbrukertype().isEmpty()) {
            return new ArrayList<>();
        }
        return lisensResource.getTilgjengeligforbrukertype()
                .stream()
                .map(Link::getHref)
                .map(href -> StringUtils.substringAfterLast(href,"/"))
                .toList();
    }
}
