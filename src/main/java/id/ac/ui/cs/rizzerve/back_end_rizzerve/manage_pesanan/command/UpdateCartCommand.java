package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateCartCommand implements CartCommand {
    private final CartService cartService;
    private final Long userId;
    private final Long menuId;
    private final int quantityChange;

    @Override
    public CartItem execute() {
        return cartService.updateCartItemQuantity(userId, menuId, quantityChange);
    }
}