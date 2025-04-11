// RemoveMenuFromCartCommand.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RemoveMenuFromCartCommand implements CartCommand {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final Long userId;
    private final Long menuId;

    @Override
    public void execute() {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Cart not found for user: " + userId));

        CartItem item = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menuId)
                .orElseThrow(() -> new IllegalStateException("Menu item not found in cart"));

        cartItemRepository.delete(item);
    }
}