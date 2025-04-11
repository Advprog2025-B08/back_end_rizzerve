package id.ac.ui.cs.rizzerve.back_end_rizzerve.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.repository.CartRepository;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Optional;

@RequiredArgsConstructor
public class UpdateCartItemCommand implements CartCommand {
    private final CartRepository cartRepository;
    private final Cart cart;
    private final Long productId;
    private final int quantityChange; // positive for increment, negative for decrement

    @Override
    public void execute() {
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantityChange;

            if (newQuantity <= 0) {
                // Remove item if quantity becomes zero or negative
                Iterator<CartItem> iterator = cart.getItems().iterator();
                while (iterator.hasNext()) {
                    CartItem cartItem = iterator.next();
                    if (cartItem.getProduct().getId().equals(productId)) {
                        iterator.remove();
                        break;
                    }
                }
            } else {
                item.setQuantity(newQuantity);
            }

            cartRepository.save(cart);
        }
    }
}