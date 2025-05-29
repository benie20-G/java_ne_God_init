package rca.ne.java.EmployeePayment_MS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductionDTO {
    private Long id;
    private String code;
    private String deductionName;
    private BigDecimal percentage;
}