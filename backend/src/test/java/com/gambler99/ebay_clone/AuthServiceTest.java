package com.gambler99.ebay_clone;

import com.gambler99.ebay_clone.dto.SignupRequestDTO;
import com.gambler99.ebay_clone.entity.Role;
import com.gambler99.ebay_clone.entity.User;
import com.gambler99.ebay_clone.repository.RoleRepository;
import com.gambler99.ebay_clone.repository.UserRepository;
import com.gambler99.ebay_clone.security.JwtTokenProvider;
import com.gambler99.ebay_clone.service.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService; // The service under test

    @Mock
    private AuthenticationManager authenticationManager; // Mocked dependency

    @Mock
    private UserRepository userRepository; // Mocked dependency for user DB operations

    @Mock
    private RoleRepository roleRepository; // Mocked dependency for role DB operations

    @Mock
    private PasswordEncoder passwordEncoder; // Mocked dependency for password encryption

    @Mock
    private JwtTokenProvider jwtTokenProvider; // Mocked dependency for JWT generation

    @BeforeEach
    public void setUp() {
        // Initializes mock objects before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        // Test user registration when username and email are available

        // Create signup request with user data
        SignupRequestDTO signupRequest = new SignupRequestDTO("buyer1", "buyer@example.com", "securePassword");

        // Mock: Username and email do not exist in DB
        when(userRepository.existsByUsername("buyer1")).thenReturn(false);
        when(userRepository.existsByEmail("buyer@example.com")).thenReturn(false);

        // Mock: Password encryption
        when(passwordEncoder.encode("securePassword")).thenReturn("hashedPassword");

        // Mock: Role retrieval from DB
        Role buyerRole = new Role();
        buyerRole.setRoleName("ROLE_BUYER");
        when(roleRepository.findByRoleName("ROLE_BUYER")).thenReturn(Optional.of(buyerRole));

        // Mock: Saving the user
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act: Call the method to register user
        authService.registerUser(signupRequest);

        // Assert: Verify that the save method was called once with a User object
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUser_AlreadyExists() {
        // Test registration fails if email already exists

        // Create signup request with user data
        SignupRequestDTO signupRequest = new SignupRequestDTO("buyer1", "buyer@example.com", "securePassword");

        // Mock: Username is available but email already exists
        when(userRepository.existsByUsername("buyer1")).thenReturn(false);
        when(userRepository.existsByEmail("buyer@example.com")).thenReturn(true);

        // Act + Assert: Verify RuntimeException is thrown with correct message
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(signupRequest);
        });

        // Assert: Exception message is correct
        assertEquals("Error: Email is already in use!", exception.getMessage());
    }

}
