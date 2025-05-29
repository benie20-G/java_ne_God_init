package rca.ne.java.EmployeePayment_MS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rca.ne.java.EmployeePayment_MS.dto.PayslipDTO;
import rca.ne.java.EmployeePayment_MS.service.PayslipService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payslips")
@Tag(name = "Payslip", description = "Payslip Management API")
public class PayslipController {
    @Autowired
    private PayslipService payslipService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get all payslips", description = "Returns a list of all payslips")
    public ResponseEntity<List<PayslipDTO>> getAllPayslips() {
        List<PayslipDTO> payslips = payslipService.getAllPayslips();
        return ResponseEntity.ok(payslips);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get payslip by ID", description = "Returns a payslip by ID")
    public ResponseEntity<PayslipDTO> getPayslipById(@PathVariable Long id) {
        PayslipDTO payslip = payslipService.getPayslipById(id);
        return ResponseEntity.ok(payslip);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER') or @securityService.isCurrentUser(#employeeId)")
    @Operation(summary = "Get payslips by employee", description = "Returns a list of payslips for a specific employee")
    public ResponseEntity<List<PayslipDTO>> getPayslipsByEmployee(@PathVariable Long employeeId) {
        List<PayslipDTO> payslips = payslipService.getPayslipsByEmployee(employeeId);
        return ResponseEntity.ok(payslips);
    }

    @GetMapping("/month-year")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get payslips by month and year", description = "Returns a list of payslips for a specific month and year")
    public ResponseEntity<List<PayslipDTO>> getPayslipsByMonthAndYear(
            @RequestParam Integer month, 
            @RequestParam Integer year) {
        List<PayslipDTO> payslips = payslipService.getPayslipsByMonthAndYear(month, year);
        return ResponseEntity.ok(payslips);
    }

    @GetMapping("/employee/{employeeId}/month-year")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER') or @securityService.isCurrentUser(#employeeId)")
    @Operation(summary = "Get payslips by employee, month and year", description = "Returns a list of payslips for a specific employee, month and year")
    public ResponseEntity<List<PayslipDTO>> getPayslipsByEmployeeAndMonthAndYear(
            @PathVariable Long employeeId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        List<PayslipDTO> payslips = payslipService.getPayslipsByEmployeeAndMonthAndYear(employeeId, month, year);
        return ResponseEntity.ok(payslips);
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @Operation(summary = "Generate payroll", description = "Generates payroll for all active employees for a specific month and year")
    public ResponseEntity<List<PayslipDTO>> generatePayroll(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        List<PayslipDTO> generatedPayslips = payslipService.generatePayroll(month, year);
        return ResponseEntity.ok(generatedPayslips);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Approve payslip", description = "Approves a specific payslip")
    public ResponseEntity<PayslipDTO> approvePayslip(@PathVariable Long id) {
        PayslipDTO approvedPayslip = payslipService.approvePayslip(id);
        return ResponseEntity.ok(approvedPayslip);
    }

    @PutMapping("/approve/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Approve all payslips", description = "Approves all pending payslips for a specific month and year")
    public ResponseEntity<List<PayslipDTO>> approveAllPayslips(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        List<PayslipDTO> approvedPayslips = payslipService.approveAllPayslips(month, year);
        return ResponseEntity.ok(approvedPayslips);
    }
}