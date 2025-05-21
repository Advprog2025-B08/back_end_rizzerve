package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetCartItemsCommand implements CartCommand {
    private final CartService cartService;
    private final Long userId;

    @Override
    public List<CartItem> execute() {
        return cartService.getCartItems(userId);
    }
}