package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RegisterRequestTest {

    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void testRegisterRequestGettersAndSetters() {
        RegisterRequest registerRequest = new RegisterRequest();
        
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        registerRequest.setRole("ADMIN");
        
        assertEquals("testuser", registerRequest.getUsername());
        assertEquals("password", registerRequest.getPassword());
        assertEquals("ADMIN", registerRequest.getRole());
    }
    
    @Test
    void testRegisterRequestDefaultValues() {
        RegisterRequest registerRequest = new RegisterRequest();
        
        assertNull(registerRequest.getUsername());
        assertNull(registerRequest.getPassword());
        assertNull(registerRequest.getRole());
    }
    
    @Test
    void testRegisterRequestValidation_WhenUsernameIsBlank() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(""); 
        registerRequest.setPassword("password");
        registerRequest.setRole("ADMIN");
        
        var violations = validator.validate(registerRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testRegisterRequestValidation_WhenUsernameIsNull() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(null); 
        registerRequest.setPassword("password");
        registerRequest.setRole("ADMIN");
        
        var violations = validator.validate(registerRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testRegisterRequestValidation_WhenPasswordIsBlank() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword(""); 
        registerRequest.setRole("ADMIN");
        
        var violations = validator.validate(registerRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testRegisterRequestValidation_WhenPasswordIsNull() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword(null); 
        registerRequest.setRole("ADMIN");
        
        var violations = validator.validate(registerRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testRegisterRequestValidation_WhenRoleIsBlank() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        registerRequest.setRole(""); 
        
        var violations = validator.validate(registerRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testRegisterRequestValidation_WhenRoleIsNull() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        registerRequest.setRole(null); 
        
        var violations = validator.validate(registerRequest);
        
        assertEquals(1, violations.size());
    }
    
    @Test
    void testRegisterRequestValidation_WhenAllFieldsAreValid() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password");
        registerRequest.setRole("ADMIN");
        
        var violations = validator.validate(registerRequest);
        
        assertEquals(0, violations.size());
    }
}