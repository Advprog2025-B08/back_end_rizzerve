package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @Test
    void testMenuGettersAndSetters() {
        Menu menu = new Menu();
        LocalDateTime now = LocalDateTime.now();
        
        menu.setId(1L);
        menu.setName("Test Menu");
        menu.setDescription("Test Description");
        menu.setUrl("/test");
        menu.setIcon("test-icon");
        menu.setDisplayOrder(1);
        menu.setIsActive(true);
        menu.setCreatedAt(now);
        menu.setUpdatedAt(now);
        
        assertEquals(1L, menu.getId());
        assertEquals("Test Menu", menu.getName());
        assertEquals("Test Description", menu.getDescription());
        assertEquals("/test", menu.getUrl());
        assertEquals("test-icon", menu.getIcon());
        assertEquals(1, menu.getDisplayOrder());
        assertTrue(menu.getIsActive());
        assertEquals(now, menu.getCreatedAt());
        assertEquals(now, menu.getUpdatedAt());
    }
    
    @Test
    void testMenuDefaultValues() {
        Menu menu = new Menu();
        
        assertNull(menu.getId());
        assertNull(menu.getName());
        assertNull(menu.getDescription());
        assertNull(menu.getUrl());
        assertNull(menu.getIcon());
        assertNull(menu.getDisplayOrder());
        assertTrue(menu.getIsActive()); 
        assertNull(menu.getCreatedAt());
        assertNull(menu.getUpdatedAt());
    }
    
    @Test
    void testPrePersist() {
        Menu menu = new Menu();
        
        menu.onCreate();
        
        assertNotNull(menu.getCreatedAt());
        assertNotNull(menu.getUpdatedAt());
        assertTrue(menu.getUpdatedAt().isEqual(menu.getCreatedAt()) || 
                   menu.getUpdatedAt().minusSeconds(1).isBefore(menu.getCreatedAt()));
    }
    
    @Test
    void testPreUpdate() {
        Menu menu = new Menu();
        LocalDateTime initialTime = LocalDateTime.now().minusHours(1);
        menu.setCreatedAt(initialTime);
        menu.setUpdatedAt(initialTime);
        
        menu.onUpdate();
        
        assertEquals(initialTime, menu.getCreatedAt()); 
        assertNotEquals(initialTime, menu.getUpdatedAt()); 
        assertTrue(menu.getUpdatedAt().isAfter(initialTime)); 
    }
}