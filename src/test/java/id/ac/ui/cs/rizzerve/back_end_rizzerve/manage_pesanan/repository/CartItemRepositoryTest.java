package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.repository;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.Cart;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model.CartItem;
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

    @Autowired
    private CartRepository cartRepository;

    @Test
    void whenFindByCartIdAndMenuId_thenReturnCartItem() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");
        entityManager.persist(user);
        entityManager.flush();

        Menu menu = new Menu();
        menu.setName("Test Menu");
        menu.setDescription("Test Description");
        entityManager.persist(menu);
        entityManager.flush();

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        entityManager.persist(cart);
        entityManager.flush();

        CartItem cartItem = new CartItem();
        cartItem.setCartId(cart.getId());
        cartItem.setMenuId(menu.getId());
        cartItem.setQuantity(2);
        entityManager.persist(cartItem);
        entityManager.flush();

        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menu.getId());

        assertTrue(found.isPresent());
        assertEquals(cart.getId(), found.get().getCartId());
        assertEquals(menu.getId(), found.get().getMenuId());
        assertEquals(2, found.get().getQuantity());
    }

    @Test
    void whenFindByInvalidCartIdAndMenuId_thenReturnEmpty() {
        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuId(99L, 999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testSaveCartItem() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");
        entityManager.persist(user);
        entityManager.flush();

        Menu menu = new Menu();
        menu.setName("Test Menu");
        menu.setDescription("Test Description");
        entityManager.persist(menu);
        entityManager.flush();

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        entityManager.persist(cart);
        entityManager.flush();

        CartItem cartItem = new CartItem();
        cartItem.setCartId(cart.getId());
        cartItem.setMenuId(menu.getId());
        cartItem.setQuantity(3);

        CartItem saved = cartItemRepository.save(cartItem);

        assertNotNull(saved.getId());
        assertEquals(cart.getId(), saved.getCartId());
        assertEquals(menu.getId(), saved.getMenuId());
        assertEquals(3, saved.getQuantity());
    }

    @Test
    void testDeleteCartItem() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");
        entityManager.persist(user);
        entityManager.flush();

        Menu menu = new Menu();
        menu.setName("Test Menu");
        menu.setDescription("Test Description");
        entityManager.persist(menu);
        entityManager.flush();

        Cart cart = new Cart();
        cart.setUserId(user.getId());
        entityManager.persist(cart);
        entityManager.flush();

        CartItem cartItem = new CartItem();
        cartItem.setCartId(cart.getId());
        cartItem.setMenuId(menu.getId());
        cartItem.setQuantity(1);
        entityManager.persist(cartItem);
        entityManager.flush();

        cartItemRepository.delete(cartItem);
        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menu.getId());

        assertFalse(found.isPresent());
    }
}