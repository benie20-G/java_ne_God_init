package rca.ne.java.EmployeePayment_MS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rca.ne.java.EmployeePayment_MS.dto.EmploymentDTO;
import rca.ne.java.EmployeePayment_MS.service.EmploymentService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employments")
@Tag(name = "Employment", description = "Employment Management API")
public class EmploymentController {
    @Autowired
    private EmploymentService employmentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get all employments", description = "Returns a list of all employments")
    public ResponseEntity<List<EmploymentDTO>> getAllEmployments() {
        List<EmploymentDTO> employments = employmentService.getAllEmployments();
        return ResponseEntity.ok(employments);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get active employments", description = "Returns a list of all active employments")
    public ResponseEntity<List<EmploymentDTO>> getActiveEmployments() {
        List<EmploymentDTO> employments = employmentService.getActiveEmployments();
        return ResponseEntity.ok(employments);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER') or @securityService.isCurrentUser(#employeeId)")
    @Operation(summary = "Get employments by employee", description = "Returns a list of employments for a specific employee")
    public ResponseEntity<List<EmploymentDTO>> getEmploymentsByEmployee(@PathVariable Long employeeId) {
        List<EmploymentDTO> employments = employmentService.getEmploymentsByEmployee(employeeId);
        return ResponseEntity.ok(employments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get employment by ID", description = "Returns an employment by ID")
    public ResponseEntity<EmploymentDTO> getEmploymentById(@PathVariable Long id) {
        EmploymentDTO employment = employmentService.getEmploymentById(id);
        return ResponseEntity.ok(employment);
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get employment by code", description = "Returns an employment by code")
    public ResponseEntity<EmploymentDTO> getEmploymentByCode(@PathVariable String code) {
        EmploymentDTO employment = employmentService.getEmploymentByCode(code);
        return ResponseEntity.ok(employment);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Create employment", description = "Creates a new employment")
    public ResponseEntity<EmploymentDTO> createEmployment(@Valid @RequestBody EmploymentDTO employmentDTO) {
        EmploymentDTO createdEmployment = employmentService.createEmployment(employmentDTO);
        return ResponseEntity.ok(createdEmployment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Update employment", description = "Updates an existing employment")
    public ResponseEntity<EmploymentDTO> updateEmployment(@PathVariable Long id, @Valid @RequestBody EmploymentDTO employmentDTO) {
        EmploymentDTO updatedEmployment = employmentService.updateEmployment(id, employmentDTO);
        return ResponseEntity.ok(updatedEmployment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Deactivate employment", description = "Deactivates an employment (sets status to INACTIVE)")
    public ResponseEntity<Void> deactivateEmployment(@PathVariable Long id) {
        employmentService.deactivateEmployment(id);
        return ResponseEntity.noContent().build();
    }
}