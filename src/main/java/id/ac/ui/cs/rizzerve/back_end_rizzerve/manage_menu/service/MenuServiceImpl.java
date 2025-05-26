package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.dto.MenuDTO;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.exception.ResourceNotFoundException;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.rating.repository.RatingRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    
    private final MenuRepository menuRepository;
    private final RatingRepository ratingRepository;
    private final CartItemRepository cartItemRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository, RatingRepository ratingRepository, CartItemRepository cartItemRepository) {
        this.menuRepository = menuRepository;
        this.ratingRepository = ratingRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<List<Menu>> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return CompletableFuture.completedFuture(menus);
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<List<Menu>> getActiveMenus() {
        List<Menu> activeMenus = menuRepository.findAllByIsActiveOrderByDisplayOrderAsc(true);
        return CompletableFuture.completedFuture(activeMenus);
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<Menu> getMenuById(Long id) {
        try {
            Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
            return CompletableFuture.completedFuture(menu);
        } catch (ResourceNotFoundException e) {
            CompletableFuture<Menu> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Async("taskExecutor")
    @Transactional
    @Override
    public CompletableFuture<Menu> createMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setName(menuDTO.getName());
        menu.setDescription(menuDTO.getDescription());
        menu.setUrl(menuDTO.getUrl());
        menu.setPrice(menuDTO.getPrice());
        menu.setIcon(menuDTO.getIcon());
        menu.setDisplayOrder(menuDTO.getDisplayOrder());
        menu.setIsActive(menuDTO.getIsActive());
        
        Menu savedMenu = menuRepository.save(menu);
        return CompletableFuture.completedFuture(savedMenu);
    }

    @Async("taskExecutor")
    @Transactional
    @Override
    public CompletableFuture<Menu> updateMenu(Long id, MenuDTO menuDTO) {
        try {
            Menu existing = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
            
            existing.setName(menuDTO.getName());
            existing.setDescription(menuDTO.getDescription());
            existing.setUrl(menuDTO.getUrl());
            existing.setPrice(menuDTO.getPrice());
            existing.setIcon(menuDTO.getIcon());
            existing.setDisplayOrder(menuDTO.getDisplayOrder());
            existing.setIsActive(menuDTO.getIsActive());
            
            Menu updated = menuRepository.save(existing);
            return CompletableFuture.completedFuture(updated);
        } catch (ResourceNotFoundException e) {
            CompletableFuture<Menu> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Async("taskExecutor")
    @Transactional
    @Override
    public CompletableFuture<Void> deleteMenu(Long id) {
        try {
            Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));
            
            ratingRepository.deleteByMenuId(id);
            
            cartItemRepository.deleteByMenuId(id);

            menuRepository.delete(menu);
            
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
}