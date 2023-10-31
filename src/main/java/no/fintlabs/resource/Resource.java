package no.fintlabs.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Resource {
    protected Long id;
    protected String resourceId;
    protected String resourceName;
    protected String resourceType;
    protected UUID identityProviderGroupObjectId;
}
