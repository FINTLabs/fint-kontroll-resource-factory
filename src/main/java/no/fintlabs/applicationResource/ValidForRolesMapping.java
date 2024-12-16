package no.fintlabs.applicationResource;

import no.fintlabs.kodeverk.Brukertype;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ValidForRolesMapping {

    public static List<String> mapValidForRolesToUserTypes(
            List<String> validForRoles,
            ApplicationResourceConfiguration applicationResourceConfiguration
    ) {
        List<String> userTypes = new ArrayList<>();

        if (validForRoles.isEmpty()) {
            userTypes.add(Brukertype.ALLTYPES.name());
            return userTypes;
        }
        List<String> studentRoles = applicationResourceConfiguration.getValidRolesForUsertype().getStudent();
        List<String> employeeFacultyRoles = applicationResourceConfiguration.getValidRolesForUsertype().getEmployeeFaculty();
        List<String> employeeStaffRoles = applicationResourceConfiguration.getValidRolesForUsertype().getEmployeeStaff();

        if (CollectionUtils.containsAny(validForRoles, studentRoles)) {
            userTypes.add(Brukertype.STUDENT.name());
        }
        if (CollectionUtils.containsAny(validForRoles, employeeFacultyRoles)) {
            userTypes.add(Brukertype.EMPLOYEEFACULTY.name());
        }
        if (CollectionUtils.containsAny(validForRoles, employeeStaffRoles)) {
            userTypes.add(Brukertype.EMPLOYEESTAFF.name());
        }
//        if (CollectionUtils.containsAny(validForRoles, studentRoles) && CollectionUtils.containsAny(validForRoles, employeeFacultyRoles)) {
//            userTypes.add(Brukertype.EDU.name());
//        }
//        if (CollectionUtils.containsAny(validForRoles, employeeFacultyRoles) && CollectionUtils.containsAny(validForRoles, employeeStaffRoles)) {
//            userTypes.add(Brukertype.EMPLOYEE.name()  );
//        }
        if (CollectionUtils.containsAny(validForRoles, studentRoles)
                && CollectionUtils.containsAny(validForRoles, employeeFacultyRoles)
                && CollectionUtils.containsAny(validForRoles, employeeStaffRoles)
        ) {
            userTypes.add(Brukertype.ALLTYPES.name());
        }
        return userTypes;
    }

}
