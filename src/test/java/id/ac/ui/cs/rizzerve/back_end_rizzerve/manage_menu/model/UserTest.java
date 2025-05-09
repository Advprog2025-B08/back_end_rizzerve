package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("ADMIN");
        
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }
    
    @Test
    void testUserDefaultValues() {
        User user = new User();
        
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }
}