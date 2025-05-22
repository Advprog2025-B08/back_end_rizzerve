package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddToCartCommand implements CartCommand {
    private final CartService cartService;
    private final Long userId;
    private final Long menuId;

    @Override
    public CartItem execute() {
        return cartService.addItemToCart(userId, menuId);
    }
}