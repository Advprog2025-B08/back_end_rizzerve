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
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
    
    @Override
    public List<Menu> getActiveMenus() {
        return menuRepository.findAllByIsActiveOrderByDisplayOrderAsc(true);
    }

    @Override
    public Menu getMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
    }

    @Transactional @Override
    public Menu createMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setName(menuDTO.getName());
        menu.setDescription(menuDTO.getDescription());
        menu.setUrl(menuDTO.getUrl());
        menu.setIcon(menuDTO.getIcon());
        menu.setDisplayOrder(menuDTO.getDisplayOrder());
        menu.setIsActive(menuDTO.getIsActive());
        
        return menuRepository.save(menu);
    }

    @Transactional @Override
    public Menu updateMenu(Long id, MenuDTO menuDTO) {
        Menu existingMenu = getMenuById(id);
        
        existingMenu.setName(menuDTO.getName());
        existingMenu.setDescription(menuDTO.getDescription());
        existingMenu.setUrl(menuDTO.getUrl());
        existingMenu.setIcon(menuDTO.getIcon());
        existingMenu.setDisplayOrder(menuDTO.getDisplayOrder());
        existingMenu.setIsActive(menuDTO.getIsActive());
        
        return menuRepository.save(existingMenu);
    }

    @Transactional @Override
    public void deleteMenu(Long id) {
        Menu menu = getMenuById(id);
        menuRepository.delete(menu);
    }
}