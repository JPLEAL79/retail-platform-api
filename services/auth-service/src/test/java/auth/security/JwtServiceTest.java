package auth.security;

import auth.entity.Role;
import auth.entity.RoleName;
import auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private static final String SECRET = "test-secret-value-with-at-least-32-chars";

    @Test
    void generateTokenIncludesUsernameAndRoles() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(SECRET);
        properties.setExpirationMs(3600000L);
        JwtService service = new JwtService(properties);

        String token = service.generateToken(user());
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals("admin", claims.getSubject());
        assertTrue(claims.get("roles", List.class).contains("ADMIN"));
        assertTrue(claims.get("roles", List.class).contains("USER"));
    }

    private User user() {
        Role userRole = role(RoleName.USER);
        Role adminRole = role(RoleName.ADMIN);

        User user = new User();
        user.setUsername("admin");
        user.setEnabled(true);
        user.setRoles(Set.of(userRole, adminRole));
        return user;
    }

    private Role role(RoleName name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
