package rca.ne.java.EmployeePayment_MS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rca.ne.java.EmployeePayment_MS.dto.PayslipDTO;
import rca.ne.java.EmployeePayment_MS.entity.Deduction;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Employment;
import rca.ne.java.EmployeePayment_MS.entity.Payslip;
import rca.ne.java.EmployeePayment_MS.entity.Payslip.PayslipStatus;
import rca.ne.java.EmployeePayment_MS.repository.DeductionRepository;
import rca.ne.java.EmployeePayment_MS.repository.EmployeeRepository;
import rca.ne.java.EmployeePayment_MS.repository.EmploymentRepository;
import rca.ne.java.EmployeePayment_MS.repository.PayslipRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PayslipService {
    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private DeductionRepository deductionRepository;

    @Autowired
    private MessageService messageService;

    public List<PayslipDTO> getAllPayslips() {
        return payslipRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PayslipDTO> getPayslipsByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        return payslipRepository.findByEmployee(employee).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PayslipDTO> getPayslipsByMonthAndYear(Integer month, Integer year) {
        return payslipRepository.findByMonthAndYear(month, year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PayslipDTO> getPayslipsByEmployeeAndMonthAndYear(Long employeeId, Integer month, Integer year) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        return payslipRepository.findByEmployeeAndMonthAndYear(employee, month, year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PayslipDTO getPayslipById(Long id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found with id: " + id));
        return convertToDTO(payslip);
    }

    @Transactional
    public List<PayslipDTO> generatePayroll(Integer month, Integer year) {
        // Get all active employments
        List<Employment> activeEmployments = employmentRepository.findByStatus(Employment.EmploymentStatus.ACTIVE);
        
        // Get all deductions
        List<Deduction> deductions = deductionRepository.findAll();
        
        // Find deductions by name
        Deduction employeeTax = findDeductionByName(deductions, "Employee Tax");
        Deduction pension = findDeductionByName(deductions, "Pension");
        Deduction medicalInsurance = findDeductionByName(deductions, "Medical Insurance");
        Deduction others = findDeductionByName(deductions, "Others");
        Deduction housing = findDeductionByName(deductions, "Housing");
        Deduction transport = findDeductionByName(deductions, "Transport");
        
        // Generate payslips for each active employment
        List<Payslip> generatedPayslips = activeEmployments.stream()
                .map(employment -> {
                    Employee employee = employment.getEmployee();
                    
                    // Check if payslip already exists for this employee, month, and year
                    if (payslipRepository.existsByEmployeeAndMonthAndYear(employee, month, year)) {
                        throw new RuntimeException("Payslip already exists for employee " + 
                                employee.getFirstName() + " " + employee.getLastName() + 
                                " for month " + month + " and year " + year);
                    }
                    
                    // Calculate payslip amounts
                    BigDecimal baseSalary = employment.getBaseSalary();
                    
                    // Calculate allowances
                    BigDecimal houseAmount = calculatePercentage(baseSalary, housing.getPercentage());
                    BigDecimal transportAmount = calculatePercentage(baseSalary, transport.getPercentage());
                    
                    // Calculate gross salary
                    BigDecimal grossSalary = baseSalary.add(houseAmount).add(transportAmount);
                    
                    // Calculate deductions
                    BigDecimal employeeTaxAmount = calculatePercentage(baseSalary, employeeTax.getPercentage());
                    BigDecimal pensionAmount = calculatePercentage(baseSalary, pension.getPercentage());
                    BigDecimal medicalInsuranceAmount = calculatePercentage(baseSalary, medicalInsurance.getPercentage());
                    BigDecimal otherAmount = calculatePercentage(baseSalary, others.getPercentage());
                    
                    // Calculate net salary
                    BigDecimal totalDeductions = employeeTaxAmount.add(pensionAmount)
                            .add(medicalInsuranceAmount).add(otherAmount);
                    BigDecimal netSalary = grossSalary.subtract(totalDeductions);
                    
                    // Create and save payslip
                    Payslip payslip = new Payslip();
                    payslip.setEmployee(employee);
                    payslip.setHouseAmount(houseAmount);
                    payslip.setTransportAmount(transportAmount);
                    payslip.setEmployeeTaxedAmount(employeeTaxAmount);
                    payslip.setPensionAmount(pensionAmount);
                    payslip.setMedicalInsuranceAmount(medicalInsuranceAmount);
                    payslip.setOtherTaxedAmount(otherAmount);
                    payslip.setGrossSalary(grossSalary);
                    payslip.setNetSalary(netSalary);
                    payslip.setMonth(month);
                    payslip.setYear(year);
                    payslip.setStatus(PayslipStatus.PENDING);
                    
                    return payslipRepository.save(payslip);
                })
                .collect(Collectors.toList());
        
        return generatedPayslips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PayslipDTO approvePayslip(Long id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found with id: " + id));
        
        if (payslip.getStatus() == PayslipStatus.PAID) {
            throw new RuntimeException("Payslip is already approved");
        }
        
        payslip.setStatus(PayslipStatus.PAID);
        Payslip approvedPayslip = payslipRepository.save(payslip);
        
        // Send notification to employee
        messageService.sendPayslipApprovalMessage(approvedPayslip);
        
        return convertToDTO(approvedPayslip);
    }

    @Transactional
    public List<PayslipDTO> approveAllPayslips(Integer month, Integer year) {
        List<Payslip> pendingPayslips = payslipRepository.findByMonthAndYearAndStatus(month, year, PayslipStatus.PENDING);
        
        List<Payslip> approvedPayslips = pendingPayslips.stream()
                .map(payslip -> {
                    payslip.setStatus(PayslipStatus.PAID);
                    Payslip approvedPayslip = payslipRepository.save(payslip);
                    
                    // Send notification to employee
                    messageService.sendPayslipApprovalMessage(approvedPayslip);
                    
                    return approvedPayslip;
                })
                .collect(Collectors.toList());
        
        return approvedPayslips.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Deduction findDeductionByName(List<Deduction> deductions, String name) {
        return deductions.stream()
                .filter(d -> d.getDeductionName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Deduction not found with name: " + name));
    }

    private BigDecimal calculatePercentage(BigDecimal amount, BigDecimal percentage) {
        return amount.multiply(percentage.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private PayslipDTO convertToDTO(Payslip payslip) {
        PayslipDTO dto = new PayslipDTO();
        dto.setId(payslip.getId());
        dto.setEmployeeId(payslip.getEmployee().getId());
        dto.setEmployeeCode(payslip.getEmployee().getCode());
        dto.setEmployeeName(payslip.getEmployee().getFirstName() + " " + payslip.getEmployee().getLastName());
        dto.setHouseAmount(payslip.getHouseAmount());
        dto.setTransportAmount(payslip.getTransportAmount());
        dto.setEmployeeTaxedAmount(payslip.getEmployeeTaxedAmount());
        dto.setPensionAmount(payslip.getPensionAmount());
        dto.setMedicalInsuranceAmount(payslip.getMedicalInsuranceAmount());
        dto.setOtherTaxedAmount(payslip.getOtherTaxedAmount());
        dto.setGrossSalary(payslip.getGrossSalary());
        dto.setNetSalary(payslip.getNetSalary());
        dto.setMonth(payslip.getMonth());
        dto.setYear(payslip.getYear());
        dto.setStatus(payslip.getStatus());
        return dto;
    }
}