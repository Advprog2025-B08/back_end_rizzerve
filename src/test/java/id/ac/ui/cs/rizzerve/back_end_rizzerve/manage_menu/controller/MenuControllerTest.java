package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.MenuDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.service.MenuService;

@ExtendWith(MockitoExtension.class)
public class MenuControllerTest {

    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

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
    void getAllActiveMenus_ShouldReturnActiveMenus() throws Exception {
        List<Menu> menus = Arrays.asList(menu);
        when(menuService.getActiveMenus()).thenReturn(CompletableFuture.completedFuture(menus));

        ResponseEntity<List<Menu>> response = menuController.getAllActiveMenus();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Menu", response.getBody().get(0).getName());
        verify(menuService, times(1)).getActiveMenus();
    }

    @Test
    void getAllMenus_ShouldReturnAllMenus() throws Exception {
        List<Menu> menus = Arrays.asList(menu);
        when(menuService.getAllMenus()).thenReturn(CompletableFuture.completedFuture(menus));

        ResponseEntity<List<Menu>> response = menuController.getAllMenus();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Menu", response.getBody().get(0).getName());
        verify(menuService, times(1)).getAllMenus();
    }

    @Test
    void getMenuById_ShouldReturnMenu() throws Exception {
        when(menuService.getMenuById(anyLong())).thenReturn(CompletableFuture.completedFuture(menu));

        ResponseEntity<Menu> response = menuController.getMenuById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Menu", response.getBody().getName());
        verify(menuService, times(1)).getMenuById(1L);
    }

    @Test
    void createMenu_ShouldCreateAndReturnMenu() throws Exception {
        when(menuService.createMenu(any(MenuDTO.class))).thenReturn(CompletableFuture.completedFuture(menu));

        ResponseEntity<Menu> response = menuController.createMenu(menuDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Menu", response.getBody().getName());
        verify(menuService, times(1)).createMenu(menuDTO);
    }

    @Test
    void updateMenu_ShouldUpdateAndReturnMenu() throws Exception {
        when(menuService.updateMenu(anyLong(), any(MenuDTO.class))).thenReturn(CompletableFuture.completedFuture(menu));

        ResponseEntity<Menu> response = menuController.updateMenu(1L, menuDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Test Menu", response.getBody().getName());
        verify(menuService, times(1)).updateMenu(1L, menuDTO);
    }

    @Test
    void deleteMenu_ShouldDeleteMenuAndReturnSuccessMessage() throws Exception {
        when(menuService.deleteMenu(anyLong())).thenReturn(CompletableFuture.completedFuture(null));

        ResponseEntity<Map<String, String>> response = menuController.deleteMenu(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Menu deleted successfully", response.getBody().get("message"));
        verify(menuService, times(1)).deleteMenu(1L);
    }
}