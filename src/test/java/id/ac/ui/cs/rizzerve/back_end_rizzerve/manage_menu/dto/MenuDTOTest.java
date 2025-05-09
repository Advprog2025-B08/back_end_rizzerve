package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MenuDTOTest {

    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void testMenuDTOGettersAndSetters() {
        MenuDTO menuDTO = new MenuDTO();
        
        menuDTO.setId(1L);
        menuDTO.setName("Test Menu");
        menuDTO.setDescription("Test Description");
        menuDTO.setUrl("/test");
        menuDTO.setIcon("test-icon");
        menuDTO.setDisplayOrder(1);
        menuDTO.setIsActive(true);
        
        assertEquals(1L, menuDTO.getId());
        assertEquals("Test Menu", menuDTO.getName());
        assertEquals("Test Description", menuDTO.getDescription());
        assertEquals("/test", menuDTO.getUrl());
        assertEquals("test-icon", menuDTO.getIcon());
        assertEquals(1, menuDTO.getDisplayOrder());
        assertTrue(menuDTO.getIsActive());
    }
    
    @Test
    void testMenuDTODefaultValues() {
        MenuDTO menuDTO = new MenuDTO();
        
        assertNull(menuDTO.getId());
        assertNull(menuDTO.getName());
        assertNull(menuDTO.getDescription());
        assertNull(menuDTO.getUrl());
        assertNull(menuDTO.getIcon());
        assertNull(menuDTO.getDisplayOrder());
        assertTrue(menuDTO.getIsActive()); 
    }
    
    @Test
    void testMenuDTOValidation_WhenNameIsBlank() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName(""); 
        
        var violations = validator.validate(menuDTO);
        
        assertEquals(1, violations.size());
        assertEquals("Menu name is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testMenuDTOValidation_WhenNameIsNull() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName(null); 
        
        var violations = validator.validate(menuDTO);
        
        assertEquals(1, violations.size());
        assertEquals("Menu name is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testMenuDTOValidation_WhenNameIsValid() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName("Valid Name"); 
        
        var violations = validator.validate(menuDTO);
        
        assertEquals(0, violations.size());
    }
}