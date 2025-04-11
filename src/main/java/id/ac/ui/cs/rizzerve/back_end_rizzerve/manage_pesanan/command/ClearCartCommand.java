package id.ac.ui.cs.rizzerve.back_end_rizzerve.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.repository.CartRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClearCartCommand implements CartCommand {
    private final CartRepository cartRepository;
    private final Cart cart;

    @Override
    public void execute() {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}