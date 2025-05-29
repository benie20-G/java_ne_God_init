package rca.ne.java.EmployeePayment_MS.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Message;
import rca.ne.java.EmployeePayment_MS.repository.EmployeeRepository;
import rca.ne.java.EmployeePayment_MS.service.MessageService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message Management API")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get all messages", description = "Returns a list of all messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER') or @securityService.isCurrentUser(#employeeId)")
    @Operation(summary = "Get messages by employee", description = "Returns a list of messages for a specific employee")
    public ResponseEntity<List<Message>> getMessagesByEmployee(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        List<Message> messages = messageService.getMessagesByEmployee(employee);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/month-year")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get messages by month and year", description = "Returns a list of messages for a specific month and year")
    public ResponseEntity<List<Message>> getMessagesByMonthAndYear(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        List<Message> messages = messageService.getMessagesByMonthAndYear(month, year);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unsent")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Get unsent messages", description = "Returns a list of unsent messages")
    public ResponseEntity<List<Message>> getUnsentMessages() {
        List<Message> messages = messageService.getUnsentMessages();
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/resend")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @Operation(summary = "Resend unsent messages", description = "Attempts to resend all unsent messages")
    public ResponseEntity<Void> resendUnsentMessages() {
        messageService.resendUnsentMessages();
        return ResponseEntity.ok().build();
    }
}