package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.MenuDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.service.MenuService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    
    private final MenuService menuService;
    
    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    
    @GetMapping
    public ResponseEntity<List<Menu>> getAllActiveMenus() {
        List<Menu> menus = menuService.getActiveMenus();
        return ResponseEntity.ok(menus);
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Menu>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }
    
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Menu> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);
        return ResponseEntity.ok(menu);
    }
    
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Menu> createMenu(@Valid @RequestBody MenuDTO menuDTO) {
        Menu createdMenu = menuService.createMenu(menuDTO);
        return new ResponseEntity<>(createdMenu, HttpStatus.CREATED);
    }
    
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Menu> updateMenu(@PathVariable Long id, @Valid @RequestBody MenuDTO menuDTO) {
        Menu updatedMenu = menuService.updateMenu(id, menuDTO);
        return ResponseEntity.ok(updatedMenu);
    }
    
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Menu deleted successfully");
        return ResponseEntity.ok(response);
    }
}