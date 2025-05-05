package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.MenuDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.exception.ResourceNotFoundException;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;

@ExtendWith(MockitoExtension.class)
public class MenuServiceImplTest {
    
    @Mock
    private MenuRepository menuRepository;
    
    @InjectMocks
    private MenuServiceImpl menuService;
    
    private Menu menu;
    private MenuDTO menuDTO;
    
    @BeforeEach
    void setUp() {
        menu = new Menu();
        menu.setId(1L);
        menu.setName("Test Menu");
        menu.setDescription("Test Description");
        menu.setUrl("/test");
        menu.setIcon("test-icon");
        menu.setDisplayOrder(1);
        menu.setIsActive(true);
        menu.setCreatedAt(LocalDateTime.now());
        menu.setUpdatedAt(LocalDateTime.now());
        
        menuDTO = new MenuDTO();
        menuDTO.setId(1L);
        menuDTO.setName("Test Menu");
        menuDTO.setDescription("Test Description");
        menuDTO.setUrl("/test");
        menuDTO.setIcon("test-icon");
        menuDTO.setDisplayOrder(1);
        menuDTO.setIsActive(true);
    }
    
    @Test
    void getAllMenus_ShouldReturnAllMenus() {
        List<Menu> menus = Arrays.asList(menu);
        when(menuRepository.findAll()).thenReturn(menus);
        
        List<Menu> result = menuService.getAllMenus();
        
        assertEquals(1, result.size());
        assertEquals("Test Menu", result.get(0).getName());
        verify(menuRepository, times(1)).findAll();
    }
    
    @Test
    void getActiveMenus_ShouldReturnActiveMenus() {
        List<Menu> activeMenus = Arrays.asList(menu);
        when(menuRepository.findAllByIsActiveOrderByDisplayOrderAsc(true)).thenReturn(activeMenus);
        
        List<Menu> result = menuService.getActiveMenus();
        
        assertEquals(1, result.size());
        assertEquals("Test Menu", result.get(0).getName());
        verify(menuRepository, times(1)).findAllByIsActiveOrderByDisplayOrderAsc(true);
    }
    
    @Test
    void getMenuById_WhenMenuExists_ShouldReturnMenu() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        
        Menu result = menuService.getMenuById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Menu", result.getName());
        verify(menuRepository, times(1)).findById(1L);
    }
    
    @Test
    void getMenuById_WhenMenuDoesNotExist_ShouldThrowException() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> menuService.getMenuById(1L));
        verify(menuRepository, times(1)).findById(1L);
    }
    
    @Test
    void createMenu_ShouldCreateAndReturnMenu() {
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);
        
        Menu result = menuService.createMenu(menuDTO);
        
        assertNotNull(result);
        assertEquals("Test Menu", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals("/test", result.getUrl());
        assertEquals("test-icon", result.getIcon());
        assertEquals(1, result.getDisplayOrder());
        assertTrue(result.getIsActive());
        verify(menuRepository, times(1)).save(any(Menu.class));
    }
    
    @Test
    void updateMenu_WhenMenuExists_ShouldUpdateAndReturnMenu() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);
        
        menuDTO.setName("Updated Menu");
        menuDTO.setDescription("Updated Description");
        
        Menu result = menuService.updateMenu(1L, menuDTO);
        
        assertNotNull(result);
        assertEquals("Updated Menu", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).save(any(Menu.class));
    }
    
    @Test
    void updateMenu_WhenMenuDoesNotExist_ShouldThrowException() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> menuService.updateMenu(1L, menuDTO));
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }
    
    @Test
    void deleteMenu_WhenMenuExists_ShouldDeleteMenu() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        doNothing().when(menuRepository).delete(any(Menu.class));
        
        menuService.deleteMenu(1L);
        
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).delete(menu);
    }
    
    @Test
    void deleteMenu_WhenMenuDoesNotExist_ShouldThrowException() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> menuService.deleteMenu(1L));
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, never()).delete(any(Menu.class));
    }
}