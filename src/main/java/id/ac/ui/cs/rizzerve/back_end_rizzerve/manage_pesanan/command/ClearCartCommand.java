package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.service.CartService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClearCartCommand implements CartCommand {
    private final CartService cartService;
    private final Long userId;

    @Override
    public Void execute() {
        cartService.clearCart(userId);
        return null;
    }
}