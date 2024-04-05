package no.fintlabs.applicationResource;

import lombok.*;
import no.fintlabs.applicationResourceLocation.ApplicationResourceLocation;
import no.fintlabs.resource.Resource;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class ApplicationResource extends Resource {
    private String applicationAccessType;
    private String applicationAccessRole;
    private List<String> platform;
    private String accessType;
    private Long resourceLimit;
    private String resourceOwnerOrgUnitId;
    private String resourceOwnerOrgUnitName;
    private List<String> validForRoles;
    private List<ApplicationResourceLocation> validForOrgUnits;
    private List<String> applicationCategory;
    private String licenseEnforcement;
    private boolean hasCost;
    private Long unitCost;
    private String status;
    private Date statusChanged;

}

