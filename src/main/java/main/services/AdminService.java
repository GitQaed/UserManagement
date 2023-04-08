package main.services;

import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<?> adminDashboard();

    ResponseEntity<?> updateRoleToUser(String username, String body);

}
