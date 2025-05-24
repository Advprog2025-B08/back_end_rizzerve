package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.MenuDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MenuService {
    CompletableFuture<List<Menu>> getAllMenus();
    CompletableFuture<List<Menu>> getActiveMenus();
    CompletableFuture<Menu> getMenuById(Long id);
    CompletableFuture<Menu> createMenu(MenuDTO menuDTO);
    CompletableFuture<Menu> updateMenu(Long id, MenuDTO menuDTO);
    CompletableFuture<Void> deleteMenu(Long id);
}
