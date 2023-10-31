package no.fintlabs.applicationResource;

import lombok.*;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocation;
import no.fintlabs.resource.Resource;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResource extends Resource {
    private String applicationAccessType;
    private String applicationAccessRole;
    private List<String> platform = new ArrayList<>();
    private String accessType;
    private Long resourceLimit;
    private String resourceOwnerOrgUnitId;
    private String resourceOwnerOrgUnitName;
    private List<String> validForRoles= new ArrayList<>();
    private List<ApplicationResourceLocation> validForOrgUnits = new ArrayList<>();

}

