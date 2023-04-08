package main.controllers;

import lombok.RequiredArgsConstructor;
import main.dtos.LoginDto;
import main.dtos.RegisterDto;
import main.services.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class UsersAuthRestController {
    private final UserAuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        return service.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return service.login(loginDto);
    }

    //Pour effectuer des tests
    @GetMapping("/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        return service.findByUsername(username);
    }

}
