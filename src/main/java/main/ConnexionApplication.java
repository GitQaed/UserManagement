
package main;

import main.entities.Role;
import main.entities.User;
import main.enums.RoleName;
import main.repositories.RoleRepository;
import main.repositories.UserRepository;
import main.services.UserAuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class ConnexionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnexionApplication.class, args);
    }

    // Set of datas for testing purpose
    @Bean
    CommandLineRunner run(UserAuthService userService, RoleRepository roleRepository,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args ->
        {
            userService.addRole(new Role(RoleName.USER));
            userService.addRole(new Role(RoleName.ADMIN));
            userService.addUser(new User("admin", passwordEncoder.encode("admin"), new ArrayList<>()));

            Role roleAdmin = roleRepository.findByRoleName(RoleName.ADMIN);
            Role roleUser = roleRepository.findByRoleName(RoleName.USER);
            User userAdmin = userRepository.findByUsername("admin").orElse(null);
            userAdmin.getRoles().add(roleAdmin);
            userAdmin.getRoles().add(roleUser);
            userService.addUser(userAdmin);
        };
    }

}
