package rca.ne.java.EmployeePayment_MS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rca.ne.java.EmployeePayment_MS.dto.EmployeeDTO;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Employee.EmployeeStatus;
import rca.ne.java.EmployeePayment_MS.repository.EmployeeRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return convertToDTO(employee);
    }

    public EmployeeDTO getEmployeeByCode(String code) {
        Employee employee = employeeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + code));
        return convertToDTO(employee);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        Employee employee = new Employee();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPassword(passwordEncoder.encode("password")); // Default password
        employee.setMobile(employeeDTO.getMobile());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setRoles(employeeDTO.getRoles());
        
        // Generate a unique code for the employee
        employee.setCode("EMP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setMobile(employeeDTO.getMobile());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setStatus(employeeDTO.getStatus());
        employee.setRoles(employeeDTO.getRoles());

        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDTO(updatedEmployee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        
        employee.setStatus(EmployeeStatus.DISABLED);
        employeeRepository.save(employee);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setCode(employee.getCode());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setMobile(employee.getMobile());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setStatus(employee.getStatus());
        dto.setRoles(employee.getRoles());
        return dto;
    }
}