package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RemoveFromCartCommand implements CartCommand {
    private final CartService cartService;
    private final Long userId;
    private final Long menuId;

    @Override
    public Void execute() {
        cartService.removeItemFromCart(userId, menuId);
        return null;
    }
}