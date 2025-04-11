// ClearCartCommand.java
package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartItemRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClearCartCommand implements CartCommand {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final Long userId;

    @Override
    public void execute() {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Cart not found for user: " + userId));

        cartItemRepository.deleteByCartId(cart.getId());
    }
}