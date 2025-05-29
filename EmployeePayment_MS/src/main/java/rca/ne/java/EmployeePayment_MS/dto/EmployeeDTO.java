package rca.ne.java.EmployeePayment_MS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rca.ne.java.EmployeePayment_MS.entity.Employee.EmployeeStatus;
import rca.ne.java.EmployeePayment_MS.entity.Employee.Role;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private LocalDate dateOfBirth;
    private EmployeeStatus status;
    private List<Role> roles;
}