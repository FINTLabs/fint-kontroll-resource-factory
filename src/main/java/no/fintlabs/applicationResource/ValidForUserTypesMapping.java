package no.fintlabs.applicationResource;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.kodeverk.Brukertype;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ValidForUserTypesMapping {

    public static List<String> mapExternalToInternalUserTypes(
            List<String> validForRoles,
            ApplicationResourceConfiguration applicationResourceConfiguration
    ) {
        List<String> userTypes = new ArrayList<>();

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
        }
        if (CollectionUtils.containsAny(validForRoles, employeeFacultyRoles)) {
            userTypes.add(Brukertype.EMPLOYEEFACULTY.name());
        }
        if (CollectionUtils.containsAny(validForRoles, employeeStaffRoles)) {
            userTypes.add(Brukertype.EMPLOYEESTAFF.name());
        }
        if (CollectionUtils.containsAny(validForRoles, allTypeRoles) || CollectionUtils.containsAny(validForRoles, studentRoles)
                && CollectionUtils.containsAny(validForRoles, employeeFacultyRoles)
                && CollectionUtils.containsAny(validForRoles, employeeStaffRoles)
        ) {
            userTypes.add(Brukertype.ALLTYPES.name());
        }
        return userTypes;
    }

    public static String mapExternalToInternalUserType(
            String externalRoleUserTypeId,
            ApplicationResourceConfiguration applicationResourceConfiguration
    ) {
        if (externalRoleUserTypeId.isEmpty()) {
            log.info("externalRoleUserTypeId is empty");
            return null;
        }
        ApplicationResourceConfiguration.ValidRolesForUsertype validRolesForUsertype = applicationResourceConfiguration.getValidRolesForUsertype();

        String studentRole = validRolesForUsertype.getStudent().getFirst();
        log.info("Student brukertypeid: {}", studentRole);
        log.info("All brukertype ID for student:");
       validRolesForUsertype.getStudent().forEach(student -> log.info(studentRole));

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
}
