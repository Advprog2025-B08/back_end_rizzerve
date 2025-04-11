package id.ac.ui.cs.rizzerve.back_end_rizzerve.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;

import java.util.Optional;

public interface CartService {
    Cart getOrCreateCartForUser(Long userId);
    void addProductToCart(Long userId, Product product);
    void updateCartItemQuantity(Long userId, Long productId, int quantityChange);
    void removeProductFromCart(Long userId, Long productId);
    void clearCart(Long userId);
    Optional<Cart> getCartByUserId(Long userId);
}