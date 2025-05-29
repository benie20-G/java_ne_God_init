package rca.ne.java.EmployeePayment_MS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Employment;
import rca.ne.java.EmployeePayment_MS.entity.Employment.EmploymentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Long> {
    Optional<Employment> findByCode(String code);
    List<Employment> findByEmployee(Employee employee);
    List<Employment> findByStatus(EmploymentStatus status);
    List<Employment> findByEmployeeAndStatus(Employee employee, EmploymentStatus status);
    boolean existsByCode(String code);
}