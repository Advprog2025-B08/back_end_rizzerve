package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.MenuDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getAllMenus();
    List<Menu> getActiveMenus();
    Menu getMenuById(Long id);
    Menu createMenu(MenuDTO menuDTO);
    Menu updateMenu(Long id, MenuDTO menuDTO);
    void deleteMenu(Long id);
}
