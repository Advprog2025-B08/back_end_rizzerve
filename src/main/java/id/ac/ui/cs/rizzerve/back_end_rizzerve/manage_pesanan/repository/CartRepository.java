package id.ac.ui.cs.rizzerve.back_end_rizzerve.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CartRepository {
    private final Map<Long, Cart> cartMap = new HashMap<>();
    private Long nextId = 1L;

    public Cart save(Cart cart) {
        if (cart.getId() == null) {
            cart.setId(nextId++);
        }
        cartMap.put(cart.getId(), cart);
        return cart;
    }

    public Optional<Cart> findById(Long id) {
        return Optional.ofNullable(cartMap.get(id));
    }

    public Optional<Cart> findByUserId(Long userId) {
        return cartMap.values().stream()
                .filter(cart -> cart.getUser() != null && cart.getUser().getId().equals(userId))
                .findFirst();
    }

    public void deleteById(Long id) {
        cartMap.remove(id);
    }

    public void clear() {
        cartMap.clear();
        nextId = 1L;
    }
}