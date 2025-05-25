package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CartService {
    // SYNC
    Cart getOrCreateCart(Long userId);
    CartItem addItemToCart(Long userId, Long menuId);
    CartItem updateCartItemQuantity(Long userId, Long menuId, int quantityChange);
    void removeItemFromCart(Long userId, Long menuId);
    List<CartItem> getCartItems(Long userId);
    void clearCart(Long userId);

    // ASYNC
    CompletableFuture<CartItem> addItemToCartAsync(Long userId, Long menuId);
    CompletableFuture<CartItem> updateCartItemQuantityAsync(Long userId, Long menuId, int quantityChange);
    CompletableFuture<Void> removeItemFromCartAsync(Long userId, Long menuId);
    CompletableFuture<List<CartItem>> getCartItemsAsync(Long userId);
    CompletableFuture<Void> clearCartAsync(Long userId);
}