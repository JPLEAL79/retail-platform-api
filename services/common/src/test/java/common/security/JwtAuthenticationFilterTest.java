package common.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JwtAuthenticationFilterTest {

    private static final String SECRET = "test-secret-key-with-enough-length-for-hmac";

    private final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(properties());

    @AfterEach
    void cleanSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void authenticatesRequestWithValidBearerToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token(List.of("USER", "ADMIN")));

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals("admin", authentication.getName());
        assertEquals(2, authentication.getAuthorities().size());
    }

    @Test
    void leavesRequestUnauthenticatedWhenTokenIsInvalid() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer bad-token");

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private JwtProperties properties() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret(SECRET);
        return properties;
    }

    private String token(List<String> roles) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 60_000);

        return Jwts.builder()
                .subject("admin")
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey())
                .compact();
    }

    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
}
