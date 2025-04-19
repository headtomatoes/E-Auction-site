package com.gambler99.ebay_clone.security;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String jwtSecret = "testSecretKey1234567890";
    private final int jwtExpirationMs = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        // Inject the jwtSecret and jwtExpirationMs values using reflection
        setField(jwtTokenProvider, "jwtSecret", jwtSecret);
        setField(jwtTokenProvider, "jwtExpirationMs", jwtExpirationMs);
    }

    /**
     * Test Case: Generate a JWT token, validate it, and extract username
     * - Ensures token generation works correctly with mocked authentication
     * - Ensures token is considered valid
     * - Ensures username can be extracted from the token
     */
    @Test
    void testGenerateAndValidateToken() {
        // Create a mock UserDetailsImpl object with basic user info and a role
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L, "testuser", "test@example.com", "password",
                Collections.singleton(() -> "ROLE_USER")
        );

        // Mock the Authentication object and return our custom user details
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Generate token from authentication
        String token = jwtTokenProvider.generateJwtToken(authentication);
        assertNotNull(token, "Generated JWT token should not be null");

        // Validate token
        assertTrue(jwtTokenProvider.validateJwtToken(token), "Generated token should be valid");

        // Extract username from token
        String username = jwtTokenProvider.getUsernameFromJwtToken(token);
        assertEquals("testuser", username, "Username from token should match the original username");
    }

    /**
     * Test Case: Invalid JWT token should be rejected
     * - Tests that a clearly invalid token fails validation
     * - Covers exception handling inside `validateJwtToken()`
     */
    @Test
    void testInvalidToken() {
        String invalidToken = "this.is.not.a.valid.token";
        assertFalse(jwtTokenProvider.validateJwtToken(invalidToken),
                "Malformed token should be rejected by validator");
    }

    // Utility method to inject private field values using reflection
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
