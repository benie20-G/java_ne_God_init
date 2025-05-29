package rca.ne.java.EmployeePayment_MS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Payslip;
import rca.ne.java.EmployeePayment_MS.entity.Payslip.PayslipStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    List<Payslip> findByEmployee(Employee employee);
    List<Payslip> findByStatus(PayslipStatus status);
    List<Payslip> findByMonthAndYear(Integer month, Integer year);
    List<Payslip> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    List<Payslip> findByEmployeeAndStatus(Employee employee, PayslipStatus status);
    List<Payslip> findByMonthAndYearAndStatus(Integer month, Integer year, PayslipStatus status);
    boolean existsByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
}