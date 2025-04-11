// CartService.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;

import java.util.List;

public interface CartService {
    void addMenuToCart(Long userId, Long menuId);
    void updateCartItemQuantity(Long userId, Long menuId, int quantityChange);
    void removeMenuFromCart(Long userId, Long menuId);
    void clearCart(Long userId);
    List<CartItem> getCartItems(Long userId);
    Cart getOrCreateCart(Long userId);
}