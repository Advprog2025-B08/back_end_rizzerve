package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository;

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
    void testFindByUserId() {
        // Given
        Long userId = 123L;
        Cart cart = Cart.builder()
                .userId(userId)
                .build();
        entityManager.persist(cart);
        entityManager.flush();

        // When
        Optional<Cart> foundCart = cartRepository.findByUserId(userId);

        // Then
        assertTrue(foundCart.isPresent());
        assertEquals(userId, foundCart.get().getUserId());
    }

    @Test
    void testFindByUserIdNotFound() {
        // Given
        Long nonExistentUserId = 999L;

        // When
        Optional<Cart> foundCart = cartRepository.findByUserId(nonExistentUserId);

        // Then
        assertFalse(foundCart.isPresent());
    }
}
