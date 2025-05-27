package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    private User user;
    private Cart cart;
    private Menu menu;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");
        entityManager.persist(user);

        cart = new Cart();
        cart.setUserId(user.getId());
        entityManager.persist(cart);

        menu = new Menu();
        menu.setName("Test Menu");
        menu.setPrice(10000L);
        entityManager.persist(menu);
    }

    @Test
    void findByUserId_ShouldReturnCart_WhenCartExists() {
        Optional<Cart> found = cartRepository.findByUserId(user.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUserId());
    }

    @Test
    void findByUserId_ShouldReturnEmpty_WhenCartNotExists() {
        Optional<Cart> found = cartRepository.findByUserId(999L); // Non-existent user ID

        assertFalse(found.isPresent());
    }

    @Test
    void findByUserIdWithItems_ShouldReturnCartWithItems_WhenCartExists() {
        CartItem item = new CartItem();
        item.setCartId(cart.getId());
        item.setMenuId(menu.getId());
        item.setQuantity(2);
        item.setCart(cart);
        item.setMenu(menu);
        entityManager.persist(item);

        Optional<Cart> found = cartRepository.findByUserIdWithItems(user.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUserId());
        assertTrue(found.get().getItems().isEmpty());
    }

    @Test
    void findByUserIdWithItems_ShouldReturnEmpty_WhenCartNotExists() {
        Optional<Cart> found = cartRepository.findByUserIdWithItems(999L);

        assertFalse(found.isPresent());
    }
}