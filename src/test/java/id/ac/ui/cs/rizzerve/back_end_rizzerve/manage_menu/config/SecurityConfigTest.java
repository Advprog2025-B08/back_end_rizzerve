package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.getClass().getName().contains("BCrypt"));
    }
    
    @Test
    void securityFilterChainBeanExists() {
        try {
            SecurityConfig.class.getDeclaredMethod("securityFilterChain", HttpSecurity.class);
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("securityFilterChain method should exist in SecurityConfig");
        }
    }
}