package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.controller;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.JwtResponse;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.LoginRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.RegisterRequest;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.security.JwtUtil;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @Mock
    private Authentication authentication;

    @Mock
    private CartService cartService;

    @InjectMocks
    private AuthController authController;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User userModel;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        registerRequest.setRole("ADMIN");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        userModel = new id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User();
        userModel.setId(1L);
        userModel.setUsername("testuser");
        userModel.setPassword("encodedPassword");
        userModel.setRole("ADMIN");
        
        userDetails = new User("testuser", "encodedPassword", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void registerUser_WhenUsernameIsAvailable_ShouldRegisterUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User.class))).thenReturn(userModel);
        // Mock cartService to avoid NullPointerException
        when(cartService.getOrCreateCart(anyLong())).thenReturn(null); // or return appropriate Cart object

        ResponseEntity<?> response = authController.registerUser(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully with cart created", response.getBody());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any(id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User.class));
        verify(cartService, times(1)).getOrCreateCart(1L);
    }

    @Test
    void registerUser_WhenUsernameIsTaken_ShouldReturnBadRequest() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userModel));

        ResponseEntity<?> response = authController.registerUser(registerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username is already taken", response.getBody());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User.class));
        verify(cartService, never()).getOrCreateCart(anyLong());
    }

    @Test
    void loginUser_WhenCredentialsAreValid_ShouldLoginSuccessfully() {
        // Mock successful authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("valid.jwt.token");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userModel));

        ResponseEntity<?> response = authController.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("valid.jwt.token", jwtResponse.getToken());
        assertEquals("testuser", jwtResponse.getUsername());
        assertEquals("ADMIN", jwtResponse.getRole());
        
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1)).generateToken(any(UserDetails.class));
    }

    @Test
    void loginUser_WhenBadCredentials_ShouldReturnUnauthorized() {
        // Mock unsuccessful authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<?> response = authController.loginUser(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, never()).generateToken(any(UserDetails.class));
    }

    @Test
    void getMe_WhenUserExists_ShouldReturnUserInfo() {
        // mock SecurityContext to return our test username
        Authentication auth = mock(Authentication.class);
        SecurityContext sc = mock(SecurityContext.class);
        when(sc.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(sc);

        // mock repository to return our userModel
        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(userModel));

        ResponseEntity<?> response = authController.getMe();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(1L, body.get("id"));
        assertEquals("testuser", body.get("username"));
        assertEquals("ADMIN", body.get("role"));
    }

}