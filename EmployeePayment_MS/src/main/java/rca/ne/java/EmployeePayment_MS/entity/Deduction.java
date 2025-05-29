package rca.ne.java.EmployeePayment_MS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "deductions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String deductionName;

    @Column(nullable = false)
    private BigDecimal percentage;

    // Helper method to calculate deduction amount based on base amount
    public BigDecimal calculateAmount(BigDecimal baseAmount) {
        return baseAmount.multiply(percentage.divide(new BigDecimal("100")));
    }
}