package auth.service;

import auth.dto.request.LoginRequest;
import auth.dto.response.LoginResponse;
import auth.entity.User;
import auth.repository.UserRepository;
import auth.security.JwtService;
import common.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final AuthService service = new AuthService(userRepository, passwordEncoder, jwtService);

    // Login succeeds only when the user exists, is enabled, and the password matches.
    @Test
    void loginReturnsBearerTokenForValidCredentials() {
        LoginRequest request = loginRequest();
        User user = user(true);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin123", "encoded-password")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        LoginResponse response = service.login(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
    }

    @Test
    void loginRejectsInvalidPassword() {
        LoginRequest request = loginRequest();
        User user = user(true);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin123", "encoded-password")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> service.login(request));
    }

    @Test
    void loginRejectsDisabledUser() {
        LoginRequest request = loginRequest();
        User user = user(false);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        assertThrows(UnauthorizedException.class, () -> service.login(request));

        verify(userRepository).findByUsername("admin");
    }

    private LoginRequest loginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");
        return request;
    }

    private User user(boolean enabled) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("encoded-password");
        user.setEnabled(enabled);
        return user;
    }
}
