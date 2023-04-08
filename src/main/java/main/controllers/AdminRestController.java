package main.controllers;

import lombok.RequiredArgsConstructor;
import main.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final AdminService service;

    @GetMapping("/dashboard")
    public ResponseEntity<?> adminDashboard() {
        return service.adminDashboard();
    }

    @PostMapping("/update-role/{username}")
    public ResponseEntity<?> updateRoleToUser(@PathVariable String username, @RequestBody String body) {
        return service.updateRoleToUser(username, body);
    }


}
