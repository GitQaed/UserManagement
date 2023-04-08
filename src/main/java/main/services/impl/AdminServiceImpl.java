package main.services.impl;

import lombok.RequiredArgsConstructor;
import main.entities.Role;
import main.entities.User;
import main.enums.RoleName;
import main.repositories.RoleRepository;
import main.repositories.UserRepository;
import main.services.AdminService;
import main.utils.CustomResponses;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static main.utils.CustomMessages.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> adminDashboard() {
        return CustomResponses.successResponse(200, ADMIN_DASHBOARD);
    }

    @Override
    public ResponseEntity<?> updateRoleToUser(String username, String body) {
        Optional<User> foundUser = userRepository.findByUsername(username);
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(body);
        } catch (Exception e) {
            return CustomResponses.errorResponse(500, PARSING_ERROR);
        }
        RoleName rn = RoleName.valueOf(json.get("roleName").toString());
        String method = json.get("method").toString();
        Role foundRole = roleRepository.findByRoleName(rn);
        if (foundUser.isPresent()) {
            try {
                if (method.equals("add")) {
                    foundUser.get().getRoles().add(foundRole);
                } else if (method.equals("remove")) {
                    foundUser.get().getRoles().remove(foundRole);
                } else {
                    return CustomResponses.errorResponse(403, UNKNOWN_METHOD);
                }
                userRepository.save(foundUser.get());
            } catch (Exception e) {
                return CustomResponses.errorResponse(500, SERVER_ERROR);
            }
        } else {
            return CustomResponses.errorResponse(404, USER_NOT_FOUND);
        }
        return CustomResponses.successResponse(200, USER_UPDATED);
    }

}
