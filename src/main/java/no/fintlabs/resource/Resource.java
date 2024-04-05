package no.fintlabs.resource;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public abstract class Resource {
    protected Long id;
    protected String resourceId;
    protected String resourceName;
    protected String resourceType;
    protected UUID identityProviderGroupObjectId;
    protected String identityProviderGroupName;
}
