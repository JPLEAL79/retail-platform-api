package auth.config;

import auth.entity.Role;
import auth.entity.RoleName;
import auth.entity.User;
import auth.repository.RoleRepository;
import auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
public class AuthDataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean seedEnabled;
    private final String adminUsername;
    private final String adminPassword;
    private final String userUsername;
    private final String userPassword;

    public AuthDataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${auth.seed.enabled}") boolean seedEnabled,
            @Value("${auth.seed.admin.username}") String adminUsername,
            @Value("${auth.seed.admin.password}") String adminPassword,
            @Value("${auth.seed.user.username}") String userUsername,
            @Value("${auth.seed.user.password}") String userPassword
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.seedEnabled = seedEnabled;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.userUsername = userUsername;
        this.userPassword = userPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!seedEnabled) {
            return;
        }

        Role userRole = getOrCreateRole(RoleName.USER);
        Role adminRole = getOrCreateRole(RoleName.ADMIN);

        createUserIfMissing(userUsername, userPassword, Set.of(userRole));
        createUserIfMissing(adminUsername, adminPassword, Set.of(userRole, adminRole));
    }

    private Role getOrCreateRole(RoleName name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(name);
                    return roleRepository.save(role);
                });
    }

    private void createUserIfMissing(String username, String rawPassword, Set<Role> roles) {
        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
        user.setRoles(roles);
        userRepository.save(user);
    }
}
