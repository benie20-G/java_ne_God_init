package rca.ne.java.EmployeePayment_MS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rca.ne.java.EmployeePayment_MS.entity.Employment.EmploymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDTO {
    private Long id;
    private String code;
    private Long employeeId;
    private String employeeCode;
    private String employeeName;
    private String department;
    private String position;
    private BigDecimal baseSalary;
    private EmploymentStatus status;
    private LocalDate joiningDate;
}