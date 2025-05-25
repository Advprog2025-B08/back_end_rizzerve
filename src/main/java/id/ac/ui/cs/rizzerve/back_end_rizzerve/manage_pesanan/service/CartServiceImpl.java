package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.MenuRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, MenuRepository menuRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(userId)
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public CartItem addItemToCart(Long userId, Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found with id: " + menuId));

        Cart cart = getOrCreateCart(userId);

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId);

        CartItem savedItem;
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            savedItem = cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cartId(cart.getId())
                    .menuId(menuId)
                    .quantity(1)
                    .build();
            savedItem = cartItemRepository.save(newItem);
        }

        return cartItemRepository.findByCartIdAndMenuIdWithMenu(cart.getId(), menuId)
                .orElse(savedItem);
    }

    @Override
    @Transactional
    public CartItem updateCartItemQuantity(Long userId, Long menuId, int quantityChange) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));

        int newQuantity = item.getQuantity() + quantityChange;

        if (newQuantity <= 0) {
            cartItemRepository.delete(item);
            return null;
        }

        item.setQuantity(newQuantity);
        CartItem savedItem = cartItemRepository.save(item);

        return cartItemRepository.findByCartIdAndMenuIdWithMenu(cart.getId(), menuId)
                .orElse(savedItem);
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long userId, Long menuId) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));

        cartItemRepository.delete(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cartItemRepository.findAllByCartIdWithMenu(cart.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void clearCart(Long userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            List<CartItem> items = cartItemRepository.findAllByCartId(cart.getId());
            if (!items.isEmpty()) {
                cartItemRepository.deleteAll(items);
            }
        }
    }

    @Override
    @Async("asyncCartExecutor")
    public CompletableFuture<CartItem> addItemToCartAsync(Long userId, Long menuId) {
        try {
            CartItem result = addItemToCart(userId, menuId);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            CompletableFuture<CartItem> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    @Async("asyncCartExecutor")
    public CompletableFuture<CartItem> updateCartItemQuantityAsync(Long userId, Long menuId, int quantityChange) {
        try {
            CartItem result = updateCartItemQuantity(userId, menuId, quantityChange);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            CompletableFuture<CartItem> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    @Async("asyncCartExecutor")
    public CompletableFuture<Void> removeItemFromCartAsync(Long userId, Long menuId) {
        try {
            removeItemFromCart(userId, menuId);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    @Async("asyncCartExecutor")
    public CompletableFuture<List<CartItem>> getCartItemsAsync(Long userId) {
        try {
            List<CartItem> result = getCartItems(userId);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            CompletableFuture<List<CartItem>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    @Async("asyncCartExecutor")
    public CompletableFuture<Void> clearCartAsync(Long userId) {
        try {
            clearCart(userId);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }
}