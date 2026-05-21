package auth.service;

import auth.dto.request.LoginRequest;
import auth.dto.response.LoginResponse;
import auth.entity.User;
import auth.repository.UserRepository;
import auth.security.JwtService;
import common.exception.ConflictException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ConflictException("Invalid username or password."));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new ConflictException("User is disabled.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ConflictException("Invalid username or password.");
        }

        return new LoginResponse(jwtService.generateToken(user));
    }
}
