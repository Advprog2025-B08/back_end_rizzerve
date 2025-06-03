package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

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
        menuDTO.setPrice(25000L);
        menuDTO.setUrl("/test");
        menuDTO.setIcon("test-icon");
        menuDTO.setDisplayOrder(1);
        menuDTO.setIsActive(true);
        
        assertEquals(1L, menuDTO.getId());
        assertEquals("Test Menu", menuDTO.getName());
        assertEquals("Test Description", menuDTO.getDescription());
        assertEquals(25000L, menuDTO.getPrice());
        assertEquals("/test", menuDTO.getUrl());
        assertEquals("test-icon", menuDTO.getIcon());
        assertEquals(1, menuDTO.getDisplayOrder());
        assertTrue(menuDTO.getIsActive());
    }
    
    @Test
    void testMenuDTODefaultConstructor() {
        MenuDTO menuDTO = new MenuDTO();
        
        assertNull(menuDTO.getId());
        assertNull(menuDTO.getName());
        assertNull(menuDTO.getDescription());
        assertEquals(0L, menuDTO.getPrice()); // primitive long defaults to 0
        assertNull(menuDTO.getUrl());
        assertNull(menuDTO.getIcon());
        assertNull(menuDTO.getDisplayOrder());
        assertTrue(menuDTO.getIsActive()); // Default value is true
    }
    
    @Test
    void testMenuDTOParameterizedConstructor() {
        Long id = 1L;
        String name = "Nasi Goreng";
        String description = "Delicious fried rice";
        long price = 15000L;
        String url = "/nasi-goreng";
        String icon = "rice-icon";
        Integer displayOrder = 5;
        Boolean isActive = true;
        
        MenuDTO menuDTO = new MenuDTO(id, name, description, price, url, icon, displayOrder, isActive);
        
        assertEquals(id, menuDTO.getId());
        assertEquals(name, menuDTO.getName());
        assertEquals(description, menuDTO.getDescription());
        assertEquals(price, menuDTO.getPrice());
        assertEquals(url, menuDTO.getUrl());
        assertEquals(icon, menuDTO.getIcon());
        assertEquals(displayOrder, menuDTO.getDisplayOrder());
        assertEquals(isActive, menuDTO.getIsActive());
    }
    
    @Test
    void testMenuDTOParameterizedConstructorWithNullValues() {
        MenuDTO menuDTO = new MenuDTO(null, null, null, 0L, null, null, null, null);
        
        assertNull(menuDTO.getId());
        assertNull(menuDTO.getName());
        assertNull(menuDTO.getDescription());
        assertEquals(0L, menuDTO.getPrice());
        assertNull(menuDTO.getUrl());
        assertNull(menuDTO.getIcon());
        assertNull(menuDTO.getDisplayOrder());
        assertNull(menuDTO.getIsActive());
    }
    
    @Test
    void testMenuDTODefaultValues() {
        MenuDTO menuDTO = new MenuDTO();
        
        assertNull(menuDTO.getId());
        assertNull(menuDTO.getName());
        assertNull(menuDTO.getDescription());
        assertEquals(0L, menuDTO.getPrice());
        assertNull(menuDTO.getUrl());
        assertNull(menuDTO.getIcon());
        assertNull(menuDTO.getDisplayOrder());
        assertTrue(menuDTO.getIsActive()); 
    }
    
    @Test
    void testMenuDTOValidation_WhenNameIsBlank() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName(""); 
        
        Set<ConstraintViolation<MenuDTO>> violations = validator.validate(menuDTO);
        
        assertEquals(1, violations.size());
        assertEquals("Menu name is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testMenuDTOValidation_WhenNameIsNull() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName(null); 
        
        Set<ConstraintViolation<MenuDTO>> violations = validator.validate(menuDTO);
        
        assertEquals(1, violations.size());
        assertEquals("Menu name is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testMenuDTOValidation_WhenNameIsWhitespace() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName("   "); // Only whitespace
        
        Set<ConstraintViolation<MenuDTO>> violations = validator.validate(menuDTO);
        
        assertEquals(1, violations.size());
        assertEquals("Menu name is required", violations.iterator().next().getMessage());
    }
    
    @Test
    void testMenuDTOValidation_WhenNameIsValid() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setName("Valid Name"); 
        
        Set<ConstraintViolation<MenuDTO>> violations = validator.validate(menuDTO);
        
        assertEquals(0, violations.size());
    }
    
    @Test
    void testMenuDTOValidation_WhenAllFieldsAreValid() {
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        menuDTO.setName("Gado-gado");
        menuDTO.setDescription("Indonesian salad");
        menuDTO.setPrice(12000L);
        menuDTO.setUrl("/gado-gado");
        menuDTO.setIcon("salad-icon");
        menuDTO.setDisplayOrder(3);
        menuDTO.setIsActive(true);
        
        Set<ConstraintViolation<MenuDTO>> violations = validator.validate(menuDTO);
        
        assertEquals(0, violations.size());
    }
    
    @Test
    void testPriceSetterAndGetter() {
        MenuDTO menuDTO = new MenuDTO();
        
        menuDTO.setPrice(50000L);
        assertEquals(50000L, menuDTO.getPrice());
        
        menuDTO.setPrice(0L);
        assertEquals(0L, menuDTO.getPrice());
        
        menuDTO.setPrice(-1000L); // Test negative price
        assertEquals(-1000L, menuDTO.getPrice());
    }
    
    @Test
    void testIsActiveSetterAndGetter() {
        MenuDTO menuDTO = new MenuDTO();
        
        // Test setting to false
        menuDTO.setIsActive(false);
        assertFalse(menuDTO.getIsActive());
        
        // Test setting to true
        menuDTO.setIsActive(true);
        assertTrue(menuDTO.getIsActive());
        
        // Test setting to null
        menuDTO.setIsActive(null);
        assertNull(menuDTO.getIsActive());
    }
    
    @Test
    void testDisplayOrderSetterAndGetter() {
        MenuDTO menuDTO = new MenuDTO();
        
        menuDTO.setDisplayOrder(10);
        assertEquals(10, menuDTO.getDisplayOrder());
        
        menuDTO.setDisplayOrder(0);
        assertEquals(0, menuDTO.getDisplayOrder());
        
        menuDTO.setDisplayOrder(-5);
        assertEquals(-5, menuDTO.getDisplayOrder());
        
        menuDTO.setDisplayOrder(null);
        assertNull(menuDTO.getDisplayOrder());
    }
    
    @Test
    void testIdSetterAndGetter() {
        MenuDTO menuDTO = new MenuDTO();
        
        menuDTO.setId(999L);
        assertEquals(999L, menuDTO.getId());
        
        menuDTO.setId(0L);
        assertEquals(0L, menuDTO.getId());
        
        menuDTO.setId(null);
        assertNull(menuDTO.getId());
    }
    
    @Test
    void testDescriptionSetterAndGetter() {
        MenuDTO menuDTO = new MenuDTO();
        
        String longDescription = "This is a very long description that contains multiple words and sentences to test the description field properly.";
        menuDTO.setDescription(longDescription);
        assertEquals(longDescription, menuDTO.getDescription());
        
        menuDTO.setDescription("");
        assertEquals("", menuDTO.getDescription());
        
        menuDTO.setDescription(null);
        assertNull(menuDTO.getDescription());
    }
    
    @Test
    void testUrlSetterAndGetter() {
        MenuDTO menuDTO = new MenuDTO();
        
        menuDTO.setUrl("/api/menu/special-dish");
        assertEquals("/api/menu/special-dish", menuDTO.getUrl());
        
        menuDTO.setUrl("");
        assertEquals("", menuDTO.getUrl());
        
        menuDTO.setUrl(null);
        assertNull(menuDTO.getUrl());
    }
    
    @Test
    void testIconSetterAndGetter() {
        MenuDTO menuDTO = new MenuDTO();
        
        menuDTO.setIcon("fa-utensils");
        assertEquals("fa-utensils", menuDTO.getIcon());
        
        menuDTO.setIcon("");
        assertEquals("", menuDTO.getIcon());
        
        menuDTO.setIcon(null);
        assertNull(menuDTO.getIcon());
    }
    
    @Test
    void testMenuDTOWithTypicalRestaurantData() {
        MenuDTO menuDTO = new MenuDTO(
            100L,
            "Rendang Daging",
            "Traditional Minang beef curry with rich coconut milk and spices",
            45000L,
            "/menu/rendang-daging",
            "beef-icon",
            1,
            true
        );
        
        assertNotNull(menuDTO);
        assertEquals(100L, menuDTO.getId());
        assertEquals("Rendang Daging", menuDTO.getName());
        assertEquals("Traditional Minang beef curry with rich coconut milk and spices", menuDTO.getDescription());
        assertEquals(45000L, menuDTO.getPrice());
        assertEquals("/menu/rendang-daging", menuDTO.getUrl());
        assertEquals("beef-icon", menuDTO.getIcon());
        assertEquals(1, menuDTO.getDisplayOrder());
        assertTrue(menuDTO.getIsActive());
    }
}