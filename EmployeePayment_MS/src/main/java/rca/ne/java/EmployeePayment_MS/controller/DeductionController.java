package rca.ne.java.EmployeePayment_MS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rca.ne.java.EmployeePayment_MS.dto.DeductionDTO;
import rca.ne.java.EmployeePayment_MS.service.DeductionService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/deductions")
@Tag(name = "Deduction", description = "Deduction Management API")
public class DeductionController {
    @Autowired
    private DeductionService deductionService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get all deductions", description = "Returns a list of all deductions")
    public ResponseEntity<List<DeductionDTO>> getAllDeductions() {
        List<DeductionDTO> deductions = deductionService.getAllDeductions();
        return ResponseEntity.ok(deductions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get deduction by ID", description = "Returns a deduction by ID")
    public ResponseEntity<DeductionDTO> getDeductionById(@PathVariable Long id) {
        DeductionDTO deduction = deductionService.getDeductionById(id);
        return ResponseEntity.ok(deduction);
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get deduction by code", description = "Returns a deduction by code")
    public ResponseEntity<DeductionDTO> getDeductionByCode(@PathVariable String code) {
        DeductionDTO deduction = deductionService.getDeductionByCode(code);
        return ResponseEntity.ok(deduction);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get deduction by name", description = "Returns a deduction by name")
    public ResponseEntity<DeductionDTO> getDeductionByName(@PathVariable String name) {
        DeductionDTO deduction = deductionService.getDeductionByName(name);
        return ResponseEntity.ok(deduction);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create deduction", description = "Creates a new deduction")
    public ResponseEntity<DeductionDTO> createDeduction(@Valid @RequestBody DeductionDTO deductionDTO) {
        DeductionDTO createdDeduction = deductionService.createDeduction(deductionDTO);
        return ResponseEntity.ok(createdDeduction);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update deduction", description = "Updates an existing deduction")
    public ResponseEntity<DeductionDTO> updateDeduction(@PathVariable Long id, @Valid @RequestBody DeductionDTO deductionDTO) {
        DeductionDTO updatedDeduction = deductionService.updateDeduction(id, deductionDTO);
        return ResponseEntity.ok(updatedDeduction);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete deduction", description = "Deletes a deduction")
    public ResponseEntity<Void> deleteDeduction(@PathVariable Long id) {
        deductionService.deleteDeduction(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/initialize")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Initialize default deductions", description = "Creates default deductions if none exist")
    public ResponseEntity<List<DeductionDTO>> initializeDefaultDeductions() {
        deductionService.initializeDefaultDeductions();
        List<DeductionDTO> deductions = deductionService.getAllDeductions();
        return ResponseEntity.ok(deductions);
    }
}