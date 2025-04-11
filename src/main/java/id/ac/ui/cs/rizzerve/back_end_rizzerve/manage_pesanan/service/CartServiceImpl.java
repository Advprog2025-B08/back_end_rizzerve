// CartServiceImpl.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public void addMenuToCart(Long userId, Long menuId) {
        CartCommand command = new AddMenuToCartCommand(cartRepository, cartItemRepository, userId, menuId);
        command.execute();
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(Long userId, Long menuId, int quantityChange) {
        CartCommand command = new UpdateCartItemQuantityCommand(cartRepository, cartItemRepository, userId, menuId, quantityChange);
        command.execute();
    }

    @Override
    @Transactional
    public void removeMenuFromCart(Long userId, Long menuId) {
        CartCommand command = new RemoveMenuFromCartCommand(cartRepository, cartItemRepository, userId, menuId);
        command.execute();
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        CartCommand command = new ClearCartCommand(cartRepository, cartItemRepository, userId);
        command.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null) {
            return new ArrayList<>();
        }
        return cartItemRepository.findByCartId(cart.getId());
    }

    @Override
    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().userId(userId).build();
                    return cartRepository.save(newCart);
                });
    }
}