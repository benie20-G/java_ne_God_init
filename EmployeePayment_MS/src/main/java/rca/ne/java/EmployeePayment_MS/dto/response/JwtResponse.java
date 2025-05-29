package rca.ne.java.EmployeePayment_MS.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rca.ne.java.EmployeePayment_MS.entity.Employee.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String code;
    private String email;
    private String firstName;
    private String lastName;
    private List<Role> roles;

    public JwtResponse(String token, Long id, String code, String email, String firstName, String lastName, List<Role> roles) {
        this.token = token;
        this.id = id;
        this.code = code;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }
}