package com.gambler99.ebay_clone;

import com.gambler99.ebay_clone.dto.JwtResponseDTO;
import com.gambler99.ebay_clone.dto.LoginRequestDTO;
import com.gambler99.ebay_clone.dto.SignupRequestDTO;
import com.gambler99.ebay_clone.service.AuthService;
import com.gambler99.ebay_clone.entity.Role;
import com.gambler99.ebay_clone.repository.RoleRepository;
import com.gambler99.ebay_clone.repository.UserRepository;
import com.gambler99.ebay_clone.security.JwtTokenProvider;
import com.gambler99.ebay_clone.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    // Inject the AuthService to test its methods
    @InjectMocks
    private AuthService authService;

    // Mock the dependencies for AuthService
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    // Setup method to initialize mocks before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    // Test the registerUser method of AuthService
    @Test
    void testRegisterUser() { 
        // Prepare a mock SignupRequestDTO (which contains the registration details)
        SignupRequestDTO signupRequest = new SignupRequestDTO("user", "email@example.com", "password123");

        // Mock repository responses: assume username and email are not already taken
        when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        // Call the registerUser method and check the response message
        String message = authService.registerUser(signupRequest).getMessage();

        // Assert that the registration was successful
        assertEquals("User registered successfully!", message);
    }

    // Test the authenticateUser method of AuthService
    @Test
    void testAuthenticateUser() {
        // Prepare a mock LoginRequestDTO (login details)
        LoginRequestDTO loginRequest = new LoginRequestDTO("user", "password123");

        // Create a mock UserDetailsImpl object for the authenticated user (mocking the user principal)
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "user", "email@example.com", "password123", Set.of());

        // Mock authentication manager to return an authenticated user
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock the JWT token generation process
        when(jwtTokenProvider.generateJwtToken(authentication)).thenReturn("testJwtToken");

        // Call authenticateUser and retrieve the JWT response
        JwtResponseDTO jwtResponse = authService.authenticateUser(loginRequest);

        // Assert that the JWT response contains the expected token and user details
        assertNotNull(jwtResponse);
        assertEquals("testJwtToken", jwtResponse.getToken()); // Verify the token is correct
        assertEquals("user", jwtResponse.getUsername()); // Verify the username is correct
    }
}
