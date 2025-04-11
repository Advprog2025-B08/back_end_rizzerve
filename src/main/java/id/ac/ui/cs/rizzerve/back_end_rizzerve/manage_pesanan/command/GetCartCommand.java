package id.ac.ui.cs.rizzerve.back_end_rizzerve.command;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.repository.CartRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GetCartCommand implements CartCommand {
    private final CartRepository cartRepository;
    private final Long userId;

    @Getter
    private Optional<Cart> result;

    @Override
    public void execute() {
        result = cartRepository.findByUserId(userId);
    }
}