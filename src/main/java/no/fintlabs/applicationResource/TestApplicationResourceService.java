package no.fintlabs.applicationResource;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocation;
import no.fintlabs.cache.FintCache;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.ApplikasjonResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisenstilgangResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.BrukertypeResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.LisensmodellResource;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.kodeverk.PlattformResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TestApplicationResourceService {
    private final ApplicationResourceEntityProducerService applicationResourceEntityProducerService;
    private final FintCache<String, ApplikasjonResource> applikasjonResourceFintCache;
    private final FintCache<String, LisensResource> lisensResourceFintCache;
    private final FintCache<String, LisenstilgangResource> lisenstilgangResourceFintCache;
    private final FintCache<String, BrukertypeResource> brukertypeResourceFintCache;
    private final FintCache<String, LisensmodellResource> lisensmodellResourceFintCache;
    private final FintCache<String, PlattformResource> plattformResourceFintCache;

    public TestApplicationResourceService(ApplicationResourceEntityProducerService applicationResourceEntityProducerService, FintCache<String, ApplikasjonResource> applikasjonResourceFintCache, FintCache<String, LisensResource> lisensResourceFintCache, FintCache<String, LisenstilgangResource> lisenstilgangResourceFintCache, FintCache<String, BrukertypeResource> brukertypeResourceFintCache, FintCache<String, LisensmodellResource> lisensmodellResourceFintCache, FintCache<String, PlattformResource> plattformResourceFintCache) {
        this.applicationResourceEntityProducerService = applicationResourceEntityProducerService;
        this.applikasjonResourceFintCache = applikasjonResourceFintCache;
        this.lisensResourceFintCache = lisensResourceFintCache;
        this.lisenstilgangResourceFintCache = lisenstilgangResourceFintCache;
        this.brukertypeResourceFintCache = brukertypeResourceFintCache;
        this.lisensmodellResourceFintCache = lisensmodellResourceFintCache;
        this.plattformResourceFintCache = plattformResourceFintCache;
    }

//
//    @Scheduled(
//            initialDelayString = "${fint.kontroll.user.publishing.initial-delay}",
//            fixedDelayString = "${fint.kontroll.user.publishing.fixed-delay}"
//    )
    public void testfintObjectToApplicationResourse(){
        log.info("Starting ...... ");
        log.info("Number of resources: "+ applikasjonResourceFintCache.getNumberOfDistinctValues());
        List<ApplikasjonResource> applikasjonResources = applikasjonResourceFintCache.getAllDistinct();
        log.info("Lengde på liste: " + applikasjonResources.size());
        applikasjonResources.forEach(applikasjonResource -> {
            System.out.println("getApplikasjonsnavn: "+applikasjonResource.getApplikasjonsnavn());
            System.out.println("getBeskrivelse: "+applikasjonResource.getBeskrivelse());
            System.out.println("getLisens: "+applikasjonResource.getLisens());
            System.out.println("getSystemid: "+applikasjonResource.getSystemid());
            System.out.println("getLinks: "+applikasjonResource.getLinks());
            System.out.println("getStottetplattform: "+applikasjonResource.getStottetplattform());
            System.out.println("getApplikasjonskategori: "+applikasjonResource.getApplikasjonskategori());
            System.out.println("getSelfLink: "+applikasjonResource.getSelfLinks());
        });

        System.out.println("*************************************");
        List<LisensResource> lisensResources = lisensResourceFintCache.getAllDistinct();
        log.info("Antall lisenser: " + lisensResources.size());
        lisensResources.forEach(lisensResource -> {
            System.out.println("getSystemid: "+ lisensResource.getSystemid());
            System.out.println("getLisensnavn: "+ lisensResource.getLisensnavn());
            System.out.println("getLisensantall: "+ lisensResource.getLisensantall());
            System.out.println("getBeskrivelse: "+ lisensResource.getBeskrivelse());
            System.out.println("getLinks: "+ lisensResource.getLinks());
            System.out.println("getApplikasjon: "+ lisensResource.getApplikasjon());
            System.out.println("getLisenstype: "+ lisensResource.getLisensmodell());
            System.out.println("getTilgjengeligforbrukertype: "+ lisensResource.getTilgjengeligforbrukertype());
            System.out.println("getLisenseier: "+ lisensResource.getLisenseier());
            System.out.println("getLisenstilgang: "+ lisensResource.getLisenstilgang());
            System.out.println("getSelfLink: " + lisensResource.getSelfLinks());

        });


        System.out.println("*************************************");
        List<LisenstilgangResource> lisenstilgangResources = lisenstilgangResourceFintCache.getAllDistinct();
        log.info("Antall lisenstilganger: " + lisenstilgangResources.size());
        lisenstilgangResources.forEach(lisenstilgangResource -> {
            System.out.println("getSystemid: "+lisenstilgangResource.getSystemid());
            System.out.println("getLisensantall: "+lisenstilgangResource.getLisensantall());
            System.out.println("getLinks: "+lisenstilgangResource.getLinks());
            System.out.println("getLisenskonsument: "+lisenstilgangResource.getLisenskonsument());
            System.out.println("getLisens: "+lisenstilgangResource.getLisens());
            //TODO: SelflinkS
            System.out.println("getSelflink: "+lisenstilgangResource.getSelfLinks());
        });

        System.out.println("*************************************");
        List<BrukertypeResource> brukertypeResources = brukertypeResourceFintCache.getAllDistinct();
        log.info("Antall brukertyper: " + brukertypeResources.size());
        brukertypeResources.forEach(brukertypeResource -> {
            System.out.println("getSystemid: " + brukertypeResource.getSystemid());
            System.out.println("getKode: " + brukertypeResource.getKode());
            System.out.println("getNavn: " + brukertypeResource.getNavn());
            System.out.println("getSelflink: " + brukertypeResource.getSelfLinks());

        });

        System.out.println("*************************************");
        List<LisensmodellResource> lisensmodellResources = lisensmodellResourceFintCache.getAllDistinct();
        lisensmodellResources.forEach(lisensmodellResource -> {
            System.out.println("getSystemid: " + lisensmodellResource.getSystemid());
            System.out.println("getKode: " + lisensmodellResource.getKode());
            System.out.println("getNavn: " + lisensmodellResource.getNavn());
            System.out.println("getSelflink: " + lisensmodellResource.getSelfLinks());

        });

        System.out.println("*************************************");
        List<PlattformResource> plattformResources = plattformResourceFintCache.getAllDistinct();
        plattformResources.forEach(plattformResource -> {
            System.out.println("getSystemid: " + plattformResource.getSystemid());
            System.out.println("getKode: " + plattformResource.getKode());
            System.out.println("getNavn: " + plattformResource.getNavn());
            System.out.println("getSelflink: " + plattformResource.getSelfLinks());
            //TODO: sjekk stottetplattform
        });



    }



   // @PostConstruct
//    public void init() {
//        log.info("Starting applicationResourceService....");
//
//
//        List<String> validForRolesAppRes1 = new ArrayList<>();
//        validForRolesAppRes1.add("student");
//        List<String> validForRolesAppRes2 = new ArrayList<>();
//        validForRolesAppRes2.add("employee");
//        List<String> validForRolesAppRes3 = new ArrayList<>();
//        validForRolesAppRes3.add("student");
//        validForRolesAppRes3.add("employee");
//
//        List<String> plattformAppres1 = new ArrayList<>();
//        plattformAppres1.add("WIN");
//        plattformAppres1.add("Linux");
//        List<String> plattformAppres2 = new ArrayList<>();
//        plattformAppres2.add("Mac");
//        plattformAppres2.add("WIN");
//        List<String> plattformAppres3 = new ArrayList<>();
//        plattformAppres3.add("ios");
//        plattformAppres3.add("android");
//        plattformAppres3.add("WIN");
//
//        //ApplicationResource1
//        ApplicationResource appRes1 = new ApplicationResource();
//        appRes1.setResourceId("adobek12");
//        appRes1.setResourceName("Adobe K12 Utdanning");
//        appRes1.setResourceType("ApplicationResource");
//        appRes1.setIdentityProviderGroupObjectId(UUID.fromString("735e619a-8905-4f68-9dab-b908076c097b"));
//
//        appRes1.setResourceLimit(1000L);
//        appRes1.setResourceOwnerOrgUnitId("6");
//        appRes1.setResourceOwnerOrgUnitName("KOMP Utdanning og kompetanse");
//        appRes1.setValidForRoles(validForRolesAppRes1);
//        ApplicationResourceLocation applicationResourceLocation1 = ApplicationResourceLocation
//                .builder()
//                .resourceId("adobek12")
//                .orgUnitId("194")
//                .orgUnitName("VGMIDT Midtbyen videregående skole")
//                .resourceLimit(100L)
//                .build();
//        ApplicationResourceLocation applicationResourceLocation2 = ApplicationResourceLocation
//                .builder()
//                .resourceId("adobek12")
//                .orgUnitId("198")
//                .orgUnitName("VGSTOR Storskog videregående skole")
//                .resourceLimit(200L)
//                .build();
//        List<ApplicationResourceLocation> locationsAppRes1 = new ArrayList<>();
//        locationsAppRes1.add(applicationResourceLocation1);
//        locationsAppRes1.add(applicationResourceLocation2);
//        appRes1.setValidForOrgUnits(locationsAppRes1);
//        appRes1.setApplicationAccessType("ApplikasjonTilgang");
//        appRes1.setApplicationAccessRole("Full access");
//        appRes1.setPlatform(plattformAppres1);
//        appRes1.setAccessType("device");
//        applicationResourceEntityProducerService.publish(appRes1);
//
//
//        //ApplicationResource2
//        ApplicationResource appRes2 = new ApplicationResource();
//        appRes2.setResourceId("msproject");
//        appRes2.setResourceName("Microsoft Project Enterprise");
//        appRes2.setResourceType("ApplicationResource");
//        appRes2.setIdentityProviderGroupObjectId(UUID.fromString("f1f7e61f-73cb-49c0-bb72-5b17b3083ced"));
//        appRes2.setResourceLimit(100L);
//
//        appRes2.setResourceOwnerOrgUnitId("5");
//        appRes2.setResourceOwnerOrgUnitName("FAK Finans og administrasjon");
//        appRes2.setValidForRoles(validForRolesAppRes2);
//        ApplicationResourceLocation applicationResourceLocation3 = ApplicationResourceLocation
//                .builder()
//                .resourceId("msproject")
//                .orgUnitId("26")
//                .orgUnitName("OKO Økonomiavdeling")
//                .resourceLimit(20L)
//                .build();
//        ApplicationResourceLocation applicationResourceLocation4 = ApplicationResourceLocation
//                .builder()
//                .resourceId("msproject")
//                .orgUnitId("30")
//                .orgUnitName("OKO Regnskapsseksjon")
//                .resourceLimit(30L)
//                .build();
//        List<ApplicationResourceLocation> locationsAppRes2 = new ArrayList<>();
//        locationsAppRes2.add(applicationResourceLocation3);
//        locationsAppRes2.add(applicationResourceLocation4);
//        appRes2.setValidForOrgUnits(locationsAppRes2);
//        appRes2.setApplicationAccessType("ApplikasjonTilgang");
//        appRes2.setApplicationAccessRole("Full access");
//        appRes2.setPlatform(plattformAppres2);
//        appRes2.setAccessType("device");
//        applicationResourceEntityProducerService.publish(appRes2);
//
//        //ApplicationResource3
//        ApplicationResource appRes3 = new ApplicationResource();
//        appRes3.setResourceId("mskabal");
//        appRes3.setResourceName("Microsoft Kabal");
//        appRes3.setResourceType("ApplicationResource");
//        appRes3.setIdentityProviderGroupObjectId(UUID.fromString("f08a85cf-f2da-4456-8568-bc144926cb9b"));
//        appRes3.setResourceLimit(300L);
//
//        appRes3.setResourceOwnerOrgUnitId("36");
//        appRes3.setResourceOwnerOrgUnitName("DIGIT Digitaliseringsavdeling");
//        appRes3.setValidForRoles(validForRolesAppRes3);
//        ApplicationResourceLocation applicationResourceLocation5 = ApplicationResourceLocation
//                .builder()
//                .resourceId("mskabal")
//                .orgUnitId("47")
//                .orgUnitName("DIGIT Fagtjenester")
//                .resourceLimit(70L)
//                .build();
//        ApplicationResourceLocation applicationResourceLocation6 = ApplicationResourceLocation
//                .builder()
//                .resourceId("mskabal")
//                .orgUnitId("38")
//                .orgUnitName("DIGIT Teknologiseksjon")
//                .resourceLimit(30L)
//                .build();
//        List<ApplicationResourceLocation> locationsAppRes3 = new ArrayList<>();
//        locationsAppRes3.add(applicationResourceLocation5);
//        locationsAppRes3.add(applicationResourceLocation6);
//        appRes3.setValidForOrgUnits(locationsAppRes3);
//        appRes3.setApplicationAccessType("ApplikasjonTilgang");
//        appRes3.setApplicationAccessRole("Full access");
//        appRes3.setPlatform(plattformAppres3);
//        appRes3.setAccessType("device");
//        applicationResourceEntityProducerService.publish(appRes3);
//    }
}

