package no.fintlabs.applicationResource;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fintlabs.fintResourceModels.resource.eiendeler.applikasjon.LisensResource;
import no.fintlabs.kodeverk.Brukertype;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ValidForUserTypesMapping {

    public static List<String> mapExternalToInternalUserTypes(
            LisensResource lisensResource,
            ApplicationResourceConfiguration applicationResourceConfiguration
    ) {
        List<String> userTypes = new ArrayList<>();
        List<String> validForRoles = getAvailableForUsertypeIds(lisensResource);

        log.info("Map external user types: {} to internal user types for resource {}", validForRoles, lisensResource.getLisensnavn());

        if (validForRoles.isEmpty()) {
            userTypes.add(Brukertype.ALLTYPES.name());
            return userTypes;
        }

        ApplicationResourceConfiguration.ValidRolesForUsertype validRolesForUsertype = applicationResourceConfiguration.getValidRolesForUsertype();
        List<String> studentRoles = validRolesForUsertype.getStudent();
        List<String> employeeFacultyRoles = validRolesForUsertype.getEmployeeFaculty();
        List<String> employeeStaffRoles = validRolesForUsertype.getEmployeeStaff();
        List<String> allTypeRoles = validRolesForUsertype.getAllTypes();

        if (CollectionUtils.containsAny(validForRoles, studentRoles)) {
            userTypes.add(Brukertype.STUDENT.name());
            log.info("Added {} user type for resource {}",Brukertype.STUDENT.name(), lisensResource.getLisensnavn());
        }
        if (CollectionUtils.containsAny(validForRoles, employeeFacultyRoles)) {
            userTypes.add(Brukertype.EMPLOYEEFACULTY.name());
            log.info("Added {} user type for resource {}",Brukertype.EMPLOYEEFACULTY.name(), lisensResource.getLisensnavn());
        }
        if (CollectionUtils.containsAny(validForRoles, employeeStaffRoles)) {
            userTypes.add(Brukertype.EMPLOYEESTAFF.name());
            log.info("Added {} user type for resource {}",Brukertype.EMPLOYEESTAFF.name(), lisensResource.getLisensnavn());
        }
        if (CollectionUtils.containsAny(validForRoles, allTypeRoles) || CollectionUtils.containsAny(validForRoles, studentRoles)
                && CollectionUtils.containsAny(validForRoles, employeeFacultyRoles)
                && CollectionUtils.containsAny(validForRoles, employeeStaffRoles)
        ) {
            userTypes.add(Brukertype.ALLTYPES.name());
            log.info("Added {} user type for resource {}",Brukertype.ALLTYPES.name(), lisensResource.getLisensnavn());
        }
        return userTypes;
    }

    public static String mapExternalToInternalUserType(
            String externalRoleUserTypeId,
            ApplicationResourceConfiguration applicationResourceConfiguration
    ) {
        if (externalRoleUserTypeId.isEmpty()) {
            return null;
        }
        ApplicationResourceConfiguration.ValidRolesForUsertype validRolesForUsertype = applicationResourceConfiguration.getValidRolesForUsertype();

        String studentRole = validRolesForUsertype.getStudent().getFirst();

        if (studentRole!=null && studentRole.equals(externalRoleUserTypeId)) {
            return Brukertype.STUDENT.name();
        }
        String employeeFacultyRole = validRolesForUsertype.getEmployeeFaculty().getFirst();

        if (employeeFacultyRole !=null && employeeFacultyRole.equals(externalRoleUserTypeId)) {
            return Brukertype.EMPLOYEEFACULTY.name();
        }
        String employeeStaffRole = validRolesForUsertype.getEmployeeStaff().getFirst();

        if (employeeStaffRole !=null && employeeStaffRole.equals(externalRoleUserTypeId)) {
            return Brukertype.EMPLOYEESTAFF.name();
        }
        String allTypeRole = validRolesForUsertype.getAllTypes().getFirst();

        if (allTypeRole !=null && allTypeRole.equals(externalRoleUserTypeId)) {
            return Brukertype.ALLTYPES.name();
        }
        return  null;
    }

    private static List<String> getAvailableForUsertypeIds(LisensResource lisensResource) {

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
