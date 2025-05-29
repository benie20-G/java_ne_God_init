package rca.ne.java.EmployeePayment_MS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Employee.Role;
import rca.ne.java.EmployeePayment_MS.repository.EmployeeRepository;
import rca.ne.java.EmployeePayment_MS.service.DeductionService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DeductionService deductionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default deductions
        deductionService.initializeDefaultDeductions();

        // Check if we already have users
        if (employeeRepository.count() == 0) {
            // Create admin user
            Employee admin = new Employee();
            admin.setCode("ADMIN001");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setMobile("1234567890");
            admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
            admin.setRoles(List.of(Role.ROLE_ADMIN));
            employeeRepository.save(admin);

            // Create manager user
            Employee manager = new Employee();
            manager.setCode("MANAGER001");
            manager.setFirstName("Manager");
            manager.setLastName("User");
            manager.setEmail("manager@example.com");
            manager.setPassword(passwordEncoder.encode("password"));
            manager.setMobile("1234567891");
            manager.setDateOfBirth(LocalDate.of(1991, 2, 2));
            manager.setRoles(List.of(Role.ROLE_MANAGER));
            employeeRepository.save(manager);

            // Create employee user
            Employee employee = new Employee();
            employee.setCode("EMPLOYEE001");
            employee.setFirstName("Employee");
            employee.setLastName("User");
            employee.setEmail("employee@example.com");
            employee.setPassword(passwordEncoder.encode("password"));
            employee.setMobile("1234567892");
            employee.setDateOfBirth(LocalDate.of(1992, 3, 3));
            employee.setRoles(List.of(Role.ROLE_EMPLOYEE));
            employeeRepository.save(employee);

            System.out.println("Sample users created successfully!");
        }
    }
}