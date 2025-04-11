package id.ac.ui.cs.rizzerve.back_end_rizzerve.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.Product;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartRepositoryTest {
    private CartRepository cartRepository;
    private Cart cart;
    private User user;

    @BeforeEach
    void setUp() {
        cartRepository = new CartRepository();

        user = User.builder()
                .id(1L)
                .name("TestUser")
                .build();

        cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
    }

    @Test
    void testSave() {
        Cart savedCart = cartRepository.save(cart);
        assertNotNull(savedCart.getId());
        assertEquals(1L, savedCart.getId());
    }

    @Test
    void testFindById() {
        Cart savedCart = cartRepository.save(cart);
        Optional<Cart> foundCart = cartRepository.findById(savedCart.getId());

        assertTrue(foundCart.isPresent());
        assertEquals(savedCart.getId(), foundCart.get().getId());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Cart> foundCart = cartRepository.findById(999L);
        assertFalse(foundCart.isPresent());
    }

    @Test
    void testFindByUserId() {
        Cart savedCart = cartRepository.save(cart);
        Optional<Cart> foundCart = cartRepository.findByUserId(user.getId());

        assertTrue(foundCart.isPresent());
        assertEquals(user.getId(), foundCart.get().getUser().getId());
    }

    @Test
    void testFindByUserIdNotFound() {
        Optional<Cart> foundCart = cartRepository.findByUserId(999L);
        assertFalse(foundCart.isPresent());
    }

    @Test
    void testDeleteById() {
        Cart savedCart = cartRepository.save(cart);
        cartRepository.deleteById(savedCart.getId());

        Optional<Cart> foundCart = cartRepository.findById(savedCart.getId());
        assertFalse(foundCart.isPresent());
    }

    @Test
    void testClear() {
        cartRepository.save(cart);

        User anotherUser = User.builder()
                .id(2L)
                .name("AnotherUser")
                .build();

        Cart anotherCart = new Cart();
        anotherCart.setUser(anotherUser);
        anotherCart.setItems(new ArrayList<>());

        cartRepository.save(anotherCart);

        cartRepository.clear();

        assertFalse(cartRepository.findById(1L).isPresent());
        assertFalse(cartRepository.findById(2L).isPresent());
    }

    @Test
    void testUpdate() {
        Cart savedCart = cartRepository.save(cart);

        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .build();

        CartItem cartItem = CartItem.builder()
                .product(product)
                .quantity(2)
                .build();

        savedCart.getItems().add(cartItem);

        Cart updatedCart = cartRepository.save(savedCart);

        assertEquals(1, updatedCart.getItems().size());
        assertEquals("Test Product", updatedCart.getItems().get(0).getProduct().getName());
    }
}