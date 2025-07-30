package no.fintlabs.applicationResourceLocation;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApplicationResourceLocationId implements Serializable {
    private Long applicationResourceId;
    private String orgUnitId;

}