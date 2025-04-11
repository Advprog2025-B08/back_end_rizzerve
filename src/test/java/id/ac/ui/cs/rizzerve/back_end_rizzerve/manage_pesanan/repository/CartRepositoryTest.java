package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.User;
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
        // Given
        User user = User.builder()
                .username("testuser")
                .password("password")
                .build();
        entityManager.persist(user);

        Cart cart = Cart.builder()
                .userId(user.getId())
                .build();
        entityManager.persist(cart);

        entityManager.flush();

        // When
        Optional<Cart> found = cartRepository.findByUserId(user.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(cart.getId(), found.get().getId());
        assertEquals(user.getId(), found.get().getUserId());
    }

    @Test
    void whenFindByInvalidUserId_thenReturnEmpty() {
        // Given
        Long invalidUserId = 999L;

        // When
        Optional<Cart> found = cartRepository.findByUserId(invalidUserId);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void whenSaveCart_thenCanRetrieve() {
        // Given
        User user = User.builder()
                .username("testuser2")
                .password("password")
                .build();
        entityManager.persist(user);

        Cart cart = Cart.builder()
                .userId(user.getId())
                .build();

        // When
        Cart saved = cartRepository.save(cart);
        Optional<Cart> found = cartRepository.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(user.getId(), found.get().getUserId());
    }
}