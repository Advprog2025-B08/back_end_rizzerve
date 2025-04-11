package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartItemRepository cartItemRepository;

    private Long cartId;
    private Long menuId;

    @BeforeEach
    void setUp() {
        // Create user
        User user = User.builder()
                .username("testuser")
                .password("password")
                .build();
        entityManager.persist(user);

        // Create cart
        Cart cart = Cart.builder()
                .userId(user.getId())
                .build();
        entityManager.persist(cart);
        cartId = cart.getId();

        // Create menu
        Menu menu = new Menu();
        menu.setName("Test Menu");
        entityManager.persist(menu);
        menuId = menu.getId();

        entityManager.flush();
    }

    @Test
    void whenFindByCartIdAndMenuId_thenReturnCartItem() {
        // Given
        CartItem cartItem = CartItem.builder()
                .cartId(cartId)
                .menuId(menuId)
                .quantity(2)
                .build();
        entityManager.persist(cartItem);
        entityManager.flush();

        // When
        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuId(cartId, menuId);

        // Then
        assertTrue(found.isPresent());
        assertEquals(cartItem.getId(), found.get().getId());
        assertEquals(cartId, found.get().getCartId());
        assertEquals(menuId, found.get().getMenuId());
        assertEquals(2, found.get().getQuantity());
    }

    @Test
    void whenFindByInvalidCartIdAndMenuId_thenReturnEmpty() {
        // Given
        Long invalidCartId = 999L;
        Long invalidMenuId = 999L;

        // When
        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuId(invalidCartId, invalidMenuId);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void whenSaveCartItem_thenCanRetrieve() {
        // Given
        CartItem cartItem = CartItem.builder()
                .cartId(cartId)
                .menuId(menuId)
                .quantity(3)
                .build();

        // When
        CartItem saved = cartItemRepository.save(cartItem);
        Optional<CartItem> found = cartItemRepository.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(cartId, found.get().getCartId());
        assertEquals(menuId, found.get().getMenuId());
        assertEquals(3, found.get().getQuantity());
    }
}