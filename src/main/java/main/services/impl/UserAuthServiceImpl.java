package main.services.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.dtos.LoginDto;
import main.dtos.RegisterDto;
import main.entities.Role;
import main.entities.User;
import main.enums.RoleName;
import main.repositories.RoleRepository;
import main.repositories.UserRepository;
import main.security.jwt.JwtUtilities;
import main.services.UserAuthService;
import main.utils.CustomHttpHeaders;
import main.utils.CustomResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static main.utils.CustomMessages.*;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        Optional<User> user = userRepository.findByUsername(loginDto.getUsername());
        if (user.isEmpty()) {
            return CustomResponses.errorResponse(404, USER_NOT_FOUND);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> rolesNames = user.get().getRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
        String token = jwtUtilities.generateToken(user.get().getUsername(), rolesNames);
        return ResponseEntity.ok()
                .headers(CustomHttpHeaders.addHeaderJwt(token))
                .body(USER_CONNECTED);
    }


    @Override
    public ResponseEntity<?> register(RegisterDto registerDto) {
        Optional<User> userExists = userRepository.findByUsername(registerDto.getUsername());
        if (userExists.isPresent()) {
            return CustomResponses.errorResponse(303, USER_EXISTS_ERROR);
        } else {
            Role role = roleRepository.findByRoleName(RoleName.USER);
            User user = new User(registerDto.getUsername(), passwordEncoder.encode(registerDto.getPassword()),
                    Collections.singletonList(role));
            try {
                userRepository.save(user);
                String token = jwtUtilities.generateToken(registerDto.getUsername(),
                        Collections.singletonList(role.getRoleName()));
                return ResponseEntity.ok()
                        .headers(CustomHttpHeaders.addHeaderJwt(token))
                        .body(REGISTER_SUCCESS);
            } catch (Exception e) {
                return CustomResponses.errorResponse(400, REGISTER_ERROR);
            }
        }
    }


    @Override
    public ResponseEntity<?> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return CustomResponses.successResponse(200, user.get());
        } else {
            return CustomResponses.errorResponse(404, USER_NOT_FOUND);
        }
    }

    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
}
