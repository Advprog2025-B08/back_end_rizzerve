package id.ac.ui.cs.rizzerve.back_end_rizzerve.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.repository.CartRepository;
import lombok.RequiredArgsConstructor;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.CartItem;
import java.util.Iterator;

@RequiredArgsConstructor
public class RemoveFromCartCommand implements CartCommand {
    private final CartRepository cartRepository;
    private final Cart cart;
    private final Long productId;

    @Override
    public void execute() {
        Iterator<CartItem> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
                break;
            }
        }

        cartRepository.save(cart);
    }
}