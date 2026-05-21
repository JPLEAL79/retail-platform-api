package common.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorizationHeaderProviderTest {

    private final AuthorizationHeaderProvider provider = new AuthorizationHeaderProvider();

    @AfterEach
    void resetRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void returnsAuthorizationHeaderFromCurrentRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        assertEquals("Bearer token", provider.currentValue().orElseThrow());
    }

    @Test
    void returnsEmptyWhenThereIsNoCurrentRequest() {
        assertTrue(provider.currentValue().isEmpty());
    }
}
