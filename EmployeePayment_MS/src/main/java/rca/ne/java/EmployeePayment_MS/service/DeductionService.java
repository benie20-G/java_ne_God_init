package rca.ne.java.EmployeePayment_MS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rca.ne.java.EmployeePayment_MS.dto.DeductionDTO;
import rca.ne.java.EmployeePayment_MS.entity.Deduction;
import rca.ne.java.EmployeePayment_MS.repository.DeductionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeductionService {
    @Autowired
    private DeductionRepository deductionRepository;

    public List<DeductionDTO> getAllDeductions() {
        return deductionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DeductionDTO getDeductionById(Long id) {
        Deduction deduction = deductionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deduction not found with id: " + id));
        return convertToDTO(deduction);
    }

    public DeductionDTO getDeductionByCode(String code) {
        Deduction deduction = deductionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Deduction not found with code: " + code));
        return convertToDTO(deduction);
    }

    public DeductionDTO getDeductionByName(String name) {
        Deduction deduction = deductionRepository.findByDeductionName(name)
                .orElseThrow(() -> new RuntimeException("Deduction not found with name: " + name));
        return convertToDTO(deduction);
    }

    public DeductionDTO createDeduction(DeductionDTO deductionDTO) {
        if (deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new RuntimeException("Deduction name is already in use");
        }

        Deduction deduction = new Deduction();
        deduction.setDeductionName(deductionDTO.getDeductionName());
        deduction.setPercentage(deductionDTO.getPercentage());
        
        // Generate a unique code for the deduction
        deduction.setCode("DED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Deduction savedDeduction = deductionRepository.save(deduction);
        return convertToDTO(savedDeduction);
    }

    public DeductionDTO updateDeduction(Long id, DeductionDTO deductionDTO) {
        Deduction deduction = deductionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deduction not found with id: " + id));

        // Check if the name is being changed and if it's already in use
        if (!deduction.getDeductionName().equals(deductionDTO.getDeductionName()) &&
                deductionRepository.existsByDeductionName(deductionDTO.getDeductionName())) {
            throw new RuntimeException("Deduction name is already in use");
        }

        deduction.setDeductionName(deductionDTO.getDeductionName());
        deduction.setPercentage(deductionDTO.getPercentage());

        Deduction updatedDeduction = deductionRepository.save(deduction);
        return convertToDTO(updatedDeduction);
    }

    public void deleteDeduction(Long id) {
        if (!deductionRepository.existsById(id)) {
            throw new RuntimeException("Deduction not found with id: " + id);
        }
        deductionRepository.deleteById(id);
    }

    // Initialize default deductions
    public void initializeDefaultDeductions() {
        if (deductionRepository.count() == 0) {
            createDeduction(new DeductionDTO(null, null, "Employee Tax", new BigDecimal("30")));
            createDeduction(new DeductionDTO(null, null, "Pension", new BigDecimal("6")));
            createDeduction(new DeductionDTO(null, null, "Medical Insurance", new BigDecimal("5")));
            createDeduction(new DeductionDTO(null, null, "Others", new BigDecimal("5")));
            createDeduction(new DeductionDTO(null, null, "Housing", new BigDecimal("14")));
            createDeduction(new DeductionDTO(null, null, "Transport", new BigDecimal("14")));
        }
    }

    private DeductionDTO convertToDTO(Deduction deduction) {
        DeductionDTO dto = new DeductionDTO();
        dto.setId(deduction.getId());
        dto.setCode(deduction.getCode());
        dto.setDeductionName(deduction.getDeductionName());
        dto.setPercentage(deduction.getPercentage());
        return dto;
    }
}