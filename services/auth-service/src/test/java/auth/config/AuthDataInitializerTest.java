package auth.config;

import auth.entity.Role;
import auth.entity.RoleName;
import auth.repository.RoleRepository;
import auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthDataInitializerTest {

    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    // Local seed creates testable users without requiring manual SQL.
    @Test
    void runCreatesDefaultRolesAndUsersWhenMissing() {
        when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleName.ADMIN)).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("admin123")).thenReturn("encoded-admin");
        when(passwordEncoder.encode("user123")).thenReturn("encoded-user");

        initializer(true).run(null);

        verify(roleRepository).findByName(RoleName.USER);
        verify(roleRepository).findByName(RoleName.ADMIN);
        verify(userRepository, times(2)).save(any());
    }

    @Test
    void runDoesNothingWhenSeedIsDisabled() {
        initializer(false).run(null);

        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any());
    }

    private AuthDataInitializer initializer(boolean enabled) {
        return new AuthDataInitializer(
                roleRepository,
                userRepository,
                passwordEncoder,
                enabled,
                "admin",
                "admin123",
                "user",
                "user123"
        );
    }
}
