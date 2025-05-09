package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    @Test
    void whenFindByUserId_thenReturnCart() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");
        entityManager.persist(user);
        entityManager.flush();

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        entityManager.persist(cart);
        entityManager.flush();

        Optional<Cart> found = cartRepository.findByUserId(user.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUserId());
    }

    @Test
    void whenFindByInvalidUserId_thenReturnEmpty() {
        Optional<Cart> found = cartRepository.findByUserId(99L);

        assertFalse(found.isPresent());
    }

    @Test
    void testSaveCart() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");
        entityManager.persist(user);
        entityManager.flush();

        Cart cart = new Cart();
        cart.setUserId(user.getId());

        Cart saved = cartRepository.save(cart);

        assertNotNull(saved.getId());
        assertEquals(user.getId(), saved.getUserId());
    }

    @Test
    void testDeleteCart() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");
        entityManager.persist(user);
        entityManager.flush();

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        entityManager.persist(cart);
        entityManager.flush();

        cartRepository.delete(cart);
        Optional<Cart> found = cartRepository.findByUserId(user.getId());

        assertFalse(found.isPresent());
    }
}