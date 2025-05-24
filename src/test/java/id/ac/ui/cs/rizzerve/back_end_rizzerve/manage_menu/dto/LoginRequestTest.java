package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class LoginRequestTest {

    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void testLoginRequestGettersAndSetters() {
        LoginRequest loginRequest = new LoginRequest();
        
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        
        assertEquals("testuser", loginRequest.getUsername());
        assertEquals("password", loginRequest.getPassword());
    }
    
    @Test
    void testLoginRequestDefaultValues() {
        LoginRequest loginRequest = new LoginRequest();
        
        assertNull(loginRequest.getUsername());
        assertNull(loginRequest.getPassword());
    }
    
    @Test
    void testLoginRequestValidation_WhenUsernameIsBlank() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(""); 
        loginRequest.setPassword("password");
        
        var violations = validator.validate(loginRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testLoginRequestValidation_WhenUsernameIsNull() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(null); 
        loginRequest.setPassword("password");
        
        var violations = validator.validate(loginRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testLoginRequestValidation_WhenPasswordIsBlank() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword(""); 
        
        var violations = validator.validate(loginRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testLoginRequestValidation_WhenPasswordIsNull() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword(null); 
        
        var violations = validator.validate(loginRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testLoginRequestValidation_WhenAllFieldsAreValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        
        var violations = validator.validate(loginRequest);
        
        assertEquals(0, violations.size());
    }
}