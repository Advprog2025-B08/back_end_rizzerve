package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_meja.mediator;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository.UserRepository;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RestaurantMediator {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public RestaurantMediator(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public Cart getOrCreateCartForUser(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .userId(user.getId())
                            .user(user)
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }
}