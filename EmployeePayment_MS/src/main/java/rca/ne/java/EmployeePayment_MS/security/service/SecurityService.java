package rca.ne.java.EmployeePayment_MS.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rca.ne.java.EmployeePayment_MS.entity.Employee;

@Service
public class SecurityService {

    /**
     * Checks if the current authenticated user is the user with the given ID.
     * 
     * @param userId The ID of the user to check
     * @return true if the current user is the user with the given ID, false otherwise
     */
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Employee)) {
            return false;
        }
        
        Employee currentUser = (Employee) principal;
        return currentUser.getId().equals(userId);
    }
}