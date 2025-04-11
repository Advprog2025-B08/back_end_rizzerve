package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.MenuDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.exception.ResourceNotFoundException;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository) {
        // Implement this constructor
    }

    @Override
    public List<Menu> getAllMenus() {
        // Implement this method
    }
    
    @Override
    public List<Menu> getActiveMenus() {
        // Implement this method
    }

    @Override
    public Menu getMenuById(Long id) {
        // Implement this method
    }

    @Override
    public Menu createMenu(MenuDTO menuDTO) {
        // Implement this method
    }

    @Override
    public Menu updateMenu(Long id, MenuDTO menuDTO) {
        // Implement this method
    }

    @Override
    public void deleteMenu(Long id) {
        // Implement this method
    }
}