package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository.RatingRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;

@ExtendWith(MockitoExtension.class)
public class MenuServiceImplTest {
    
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RatingRepository ratingRepository;
    
    @Mock
    private CartItemRepository cartItemRepository;
    
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
        menu.setPrice(10000);
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
        menuDTO.setPrice(10000);
        menuDTO.setUrl("/test");
        menuDTO.setIcon("test-icon");
        menuDTO.setDisplayOrder(1);
        menuDTO.setIsActive(true);
    }
    
    @Test
    void getAllMenus_ShouldReturnAllMenus() throws Exception {
        List<Menu> menus = Arrays.asList(menu);
        when(menuRepository.findAll()).thenReturn(menus);
        
        CompletableFuture<List<Menu>> futureResult = menuService.getAllMenus();
        List<Menu> result = futureResult.get();
        
        assertEquals(1, result.size());
        assertEquals("Test Menu", result.get(0).getName());
        verify(menuRepository, times(1)).findAll();
    }
    
    @Test
    void getActiveMenus_ShouldReturnActiveMenus() throws Exception {
        List<Menu> activeMenus = Arrays.asList(menu);
        when(menuRepository.findAllByIsActiveOrderByDisplayOrderAsc(true)).thenReturn(activeMenus);
        
        CompletableFuture<List<Menu>> futureResult = menuService.getActiveMenus();
        List<Menu> result = futureResult.get();
        
        assertEquals(1, result.size());
        assertEquals("Test Menu", result.get(0).getName());
        verify(menuRepository, times(1)).findAllByIsActiveOrderByDisplayOrderAsc(true);
    }
    
    @Test
    void getMenuById_WhenMenuExists_ShouldReturnMenu() throws Exception {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        
        CompletableFuture<Menu> futureResult = menuService.getMenuById(1L);
        Menu result = futureResult.get();
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Menu", result.getName());
        verify(menuRepository, times(1)).findById(1L);
    }
    
    @Test
    void getMenuById_WhenMenuDoesNotExist_ShouldThrowException() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        CompletableFuture<Menu> futureResult = menuService.getMenuById(1L);
        
        // Since @Async might not work in test context, the exception could be thrown directly
        // or wrapped in ExecutionException, so we test for both possibilities
        try {
            futureResult.get();
            // If we reach here, no exception was thrown, which is unexpected
            throw new AssertionError("Expected an exception to be thrown");
        } catch (Exception e) {
            // Check if it's either ResourceNotFoundException or ExecutionException wrapping it
            assertTrue(e instanceof ResourceNotFoundException || 
                      (e instanceof java.util.concurrent.ExecutionException && 
                       e.getCause() instanceof ResourceNotFoundException),
                      "Expected ResourceNotFoundException or ExecutionException wrapping ResourceNotFoundException, but got: " + e.getClass());
        }
        
        verify(menuRepository, times(1)).findById(1L);
    }
    
    @Test
    void createMenu_ShouldCreateAndReturnMenu() throws Exception {
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);
        
        CompletableFuture<Menu> futureResult = menuService.createMenu(menuDTO);
        Menu result = futureResult.get();
        
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
    void updateMenu_WhenMenuExists_ShouldUpdateAndReturnMenu() throws Exception {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        
        // Create updated menu object
        Menu updatedMenu = new Menu();
        updatedMenu.setId(1L);
        updatedMenu.setName("Updated Menu");
        updatedMenu.setDescription("Updated Description");
        updatedMenu.setUrl("/test");
        updatedMenu.setIcon("test-icon");
        updatedMenu.setDisplayOrder(1);
        updatedMenu.setIsActive(true);
        updatedMenu.setCreatedAt(LocalDateTime.now());
        updatedMenu.setUpdatedAt(LocalDateTime.now());
        
        when(menuRepository.save(any(Menu.class))).thenReturn(updatedMenu);
        
        menuDTO.setName("Updated Menu");
        menuDTO.setDescription("Updated Description");
        
        CompletableFuture<Menu> futureResult = menuService.updateMenu(1L, menuDTO);
        Menu result = futureResult.get();
        
        assertNotNull(result);
        assertEquals("Updated Menu", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).save(any(Menu.class));
    }
    
    @Test
    void updateMenu_WhenMenuDoesNotExist_ShouldThrowException() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        CompletableFuture<Menu> futureResult = menuService.updateMenu(1L, menuDTO);
        
        // Since @Async might not work in test context, the exception could be thrown directly
        // or wrapped in ExecutionException, so we test for both possibilities
        try {
            futureResult.get();
            // If we reach here, no exception was thrown, which is unexpected
            throw new AssertionError("Expected an exception to be thrown");
        } catch (Exception e) {
            // Check if it's either ResourceNotFoundException or ExecutionException wrapping it
            assertTrue(e instanceof ResourceNotFoundException || 
                      (e instanceof java.util.concurrent.ExecutionException && 
                       e.getCause() instanceof ResourceNotFoundException),
                      "Expected ResourceNotFoundException or ExecutionException wrapping ResourceNotFoundException, but got: " + e.getClass());
        }
        
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, never()).save(any(Menu.class));
    }
    
    @Test
    void deleteMenu_WhenMenuExists_ShouldDeleteMenu() throws Exception {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        doNothing().when(ratingRepository).deleteByMenuId(anyLong());
        doNothing().when(cartItemRepository).deleteByMenuId(anyLong());
        doNothing().when(menuRepository).delete(any(Menu.class));
        
        CompletableFuture<Void> futureResult = menuService.deleteMenu(1L);
        futureResult.get(); // Wait for completion
        
        verify(menuRepository, times(1)).findById(1L);
        verify(ratingRepository, times(1)).deleteByMenuId(1L);
        verify(cartItemRepository, times(1)).deleteByMenuId(1L);
        verify(menuRepository, times(1)).delete(menu);
    }
    
    @Test
    void deleteMenu_WhenMenuDoesNotExist_ShouldThrowException() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        CompletableFuture<Void> futureResult = menuService.deleteMenu(1L);
        
        // Since @Async might not work in test context, the exception could be thrown directly
        // or wrapped in ExecutionException, so we test for both possibilities
        try {
            futureResult.get();
            // If we reach here, no exception was thrown, which is unexpected
            throw new AssertionError("Expected an exception to be thrown");
        } catch (Exception e) {
            // Check if it's either ResourceNotFoundException or ExecutionException wrapping it
            assertTrue(e instanceof ResourceNotFoundException || 
                      (e instanceof java.util.concurrent.ExecutionException && 
                       e.getCause() instanceof ResourceNotFoundException),
                      "Expected ResourceNotFoundException or ExecutionException wrapping ResourceNotFoundException, but got: " + e.getClass());
        }
        
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, never()).delete(any(Menu.class));
    }
}