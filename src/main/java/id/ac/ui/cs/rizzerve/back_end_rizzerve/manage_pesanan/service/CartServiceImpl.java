package id.ac.ui.cs.rizzerve.back_end_rizzerve.service;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.command.*;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    public Cart getOrCreateCartForUser(Long userId) {
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            Cart newCart = new Cart();
            User user = new User();
            user.setId(userId);
            newCart.setUser(user);
            return cartRepository.save(newCart);
        }
    }

    @Override
    public void addProductToCart(Long userId, Product product) {
        Cart cart = getOrCreateCartForUser(userId);
        CartCommand command = new AddToCartCommand(cartRepository, cart, product);
        command.execute();
    }

    @Override
    public void updateCartItemQuantity(Long userId, Long productId, int quantityChange) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            CartCommand command = new UpdateCartItemCommand(cartRepository, cart, productId, quantityChange);
            command.execute();
        }
    }

    @Override
    public void removeProductFromCart(Long userId, Long productId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            CartCommand command = new RemoveFromCartCommand(cartRepository, cart, productId);
            command.execute();
        }
    }

    @Override
    public void clearCart(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            CartCommand command = new ClearCartCommand(cartRepository, cart);
            command.execute();
        }
    }

    @Override
    public Optional<Cart> getCartByUserId(Long userId) {
        GetCartCommand command = new GetCartCommand(cartRepository, userId);
        command.execute();
        return command.getResult();
    }
}