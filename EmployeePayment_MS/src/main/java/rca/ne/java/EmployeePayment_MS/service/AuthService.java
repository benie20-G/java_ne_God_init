package rca.ne.java.EmployeePayment_MS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rca.ne.java.EmployeePayment_MS.dto.request.LoginRequest;
import rca.ne.java.EmployeePayment_MS.dto.request.RegisterRequest;
import rca.ne.java.EmployeePayment_MS.dto.response.JwtResponse;
import rca.ne.java.EmployeePayment_MS.dto.response.MessageResponse;
import rca.ne.java.EmployeePayment_MS.entity.Employee;
import rca.ne.java.EmployeePayment_MS.entity.Employee.Role;
import rca.ne.java.EmployeePayment_MS.repository.EmployeeRepository;
import rca.ne.java.EmployeePayment_MS.security.jwt.JwtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        Employee employee = (Employee) authentication.getPrincipal();
        
        return new JwtResponse(
                jwt,
                employee.getId(),
                employee.getCode(),
                employee.getEmail(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getRoles());
    }

    public MessageResponse registerUser(RegisterRequest registerRequest) {
        if (employeeRepository.existsByEmail(registerRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Create new employee
        Employee employee = new Employee();
        employee.setFirstName(registerRequest.getFirstName());
        employee.setLastName(registerRequest.getLastName());
        employee.setEmail(registerRequest.getEmail());
        employee.setPassword(encoder.encode(registerRequest.getPassword()));
        employee.setMobile(registerRequest.getMobile());
        employee.setDateOfBirth(registerRequest.getDateOfBirth());
        
        // Generate a unique code for the employee
        employee.setCode("EMP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Set roles
        List<Role> roles = registerRequest.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new ArrayList<>();
            roles.add(Role.ROLE_EMPLOYEE);
        }
        employee.setRoles(roles);

        employeeRepository.save(employee);

        return new MessageResponse("User registered successfully!");
    }
}