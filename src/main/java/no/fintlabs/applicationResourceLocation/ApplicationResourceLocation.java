package no.fintlabs.applicationResourceLocation;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ApplicationResourceLocation {
    private Long id;
    private String resourceId;
    private String orgUnitId;
    private String orgUnitName;
    private Long resourceLimit;
}
