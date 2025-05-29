package rca.ne.java.EmployeePayment_MS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rca.ne.java.EmployeePayment_MS.entity.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByCode(String code);
    boolean existsByEmail(String email);
    boolean existsByCode(String code);
}