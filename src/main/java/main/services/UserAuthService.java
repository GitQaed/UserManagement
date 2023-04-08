package main.services;

import main.dtos.LoginDto;
import main.dtos.RegisterDto;
import main.entities.Role;
import main.entities.User;
import org.springframework.http.ResponseEntity;

public interface UserAuthService {
    ResponseEntity<?> login(LoginDto loginDto);

    ResponseEntity<?> register(RegisterDto registerDto);

    Role addRole(Role role);

    User addUser(User user);


    ResponseEntity<?> findByUsername(String username);
}
