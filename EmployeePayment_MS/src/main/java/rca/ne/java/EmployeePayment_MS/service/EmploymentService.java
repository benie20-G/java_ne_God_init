package rca.ne.java.EmployeePayment_MS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rca.ne.java.EmployeePayment_MS.dto.EmploymentDTO;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Employment;
import rca.ne.java.EmployeePayment_MS.entity.Employment.EmploymentStatus;
import rca.ne.java.EmployeePayment_MS.repository.EmployeeRepository;
import rca.ne.java.EmployeePayment_MS.repository.EmploymentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmploymentService {
    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmploymentDTO> getAllEmployments() {
        return employmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EmploymentDTO> getActiveEmployments() {
        return employmentRepository.findByStatus(EmploymentStatus.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EmploymentDTO> getEmploymentsByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        return employmentRepository.findByEmployee(employee).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmploymentDTO getEmploymentById(Long id) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found with id: " + id));
        return convertToDTO(employment);
    }

    public EmploymentDTO getEmploymentByCode(String code) {
        Employment employment = employmentRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Employment not found with code: " + code));
        return convertToDTO(employment);
    }

    public EmploymentDTO createEmployment(EmploymentDTO employmentDTO) {
        Employee employee = employeeRepository.findById(employmentDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employmentDTO.getEmployeeId()));

        Employment employment = new Employment();
        employment.setEmployee(employee);
        employment.setDepartment(employmentDTO.getDepartment());
        employment.setPosition(employmentDTO.getPosition());
        employment.setBaseSalary(employmentDTO.getBaseSalary());
        employment.setStatus(EmploymentStatus.ACTIVE);
        employment.setJoiningDate(employmentDTO.getJoiningDate());
        
        // Generate a unique code for the employment
        employment.setCode("EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Employment savedEmployment = employmentRepository.save(employment);
        return convertToDTO(savedEmployment);
    }

    public EmploymentDTO updateEmployment(Long id, EmploymentDTO employmentDTO) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found with id: " + id));

        employment.setDepartment(employmentDTO.getDepartment());
        employment.setPosition(employmentDTO.getPosition());
        employment.setBaseSalary(employmentDTO.getBaseSalary());
        employment.setStatus(employmentDTO.getStatus());
        employment.setJoiningDate(employmentDTO.getJoiningDate());

        Employment updatedEmployment = employmentRepository.save(employment);
        return convertToDTO(updatedEmployment);
    }

    public void deactivateEmployment(Long id) {
        Employment employment = employmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employment not found with id: " + id));
        
        employment.setStatus(EmploymentStatus.INACTIVE);
        employmentRepository.save(employment);
    }

    private EmploymentDTO convertToDTO(Employment employment) {
        EmploymentDTO dto = new EmploymentDTO();
        dto.setId(employment.getId());
        dto.setCode(employment.getCode());
        dto.setEmployeeId(employment.getEmployee().getId());
        dto.setEmployeeCode(employment.getEmployee().getCode());
        dto.setEmployeeName(employment.getEmployee().getFirstName() + " " + employment.getEmployee().getLastName());
        dto.setDepartment(employment.getDepartment());
        dto.setPosition(employment.getPosition());
        dto.setBaseSalary(employment.getBaseSalary());
        dto.setStatus(employment.getStatus());
        dto.setJoiningDate(employment.getJoiningDate());
        return dto;
    }
}