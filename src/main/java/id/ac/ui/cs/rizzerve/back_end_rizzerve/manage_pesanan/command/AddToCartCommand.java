package id.ac.ui.cs.rizzerve.back_end_rizzerve.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.repository.CartRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AddToCartCommand implements CartCommand {
    private final CartRepository cartRepository;
    private final Cart cart;
    private final Product product;

    @Override
    public void execute() {
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
        } else {
            CartItem newItem = CartItem.builder()
                    .product(product)
                    .quantity(1)
                    .build();
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }
}