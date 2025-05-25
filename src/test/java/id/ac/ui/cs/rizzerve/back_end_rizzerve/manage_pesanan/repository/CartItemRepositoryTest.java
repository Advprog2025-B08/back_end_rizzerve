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
class CartItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartItemRepository cartItemRepository;

    private User user;
    private Cart cart;
    private Menu menu1;
    private Menu menu2;
    private CartItem item1;
    private CartItem item2;

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

        menu1 = new Menu();
        menu1.setName("Menu 1");
        menu1.setPrice(10000L);
        entityManager.persist(menu1);

        menu2 = new Menu();
        menu2.setName("Menu 2");
        menu2.setPrice(15000L);
        entityManager.persist(menu2);

        item1 = new CartItem();
        item1.setCartId(cart.getId());
        item1.setMenuId(menu1.getId());
        item1.setQuantity(1);
        item1.setCart(cart);
        item1.setMenu(menu1);
        entityManager.persist(item1);

        item2 = new CartItem();
        item2.setCartId(cart.getId());
        item2.setMenuId(menu2.getId());
        item2.setQuantity(2);
        item2.setCart(cart);
        item2.setMenu(menu2);
        entityManager.persist(item2);
    }

    @Test
    void findByCartIdAndMenuId_ShouldReturnCartItem_WhenExists() {
        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuId(cart.getId(), menu1.getId());

        assertTrue(found.isPresent());
        assertEquals(cart.getId(), found.get().getCartId());
        assertEquals(menu1.getId(), found.get().getMenuId());
    }

    @Test
    void findByCartIdAndMenuId_ShouldReturnEmpty_WhenNotExists() {
        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuId(999L, 999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findByCartIdAndMenuIdWithMenu_ShouldReturnCartItemWithMenu_WhenExists() {
        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuIdWithMenu(cart.getId(), menu1.getId());

        assertTrue(found.isPresent());
        assertEquals(cart.getId(), found.get().getCartId());
        assertEquals(menu1.getId(), found.get().getMenuId());
        assertNotNull(found.get().getMenu());
        assertEquals("Menu 1", found.get().getMenu().getName());
    }

    @Test
    void findByCartIdAndMenuIdWithMenu_ShouldReturnEmpty_WhenNotExists() {
        Optional<CartItem> found = cartItemRepository.findByCartIdAndMenuIdWithMenu(999L, 999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findAllByCartIdWithMenu_ShouldReturnCartItemsWithMenu_WhenExist() {
        List<CartItem> found = cartItemRepository.findAllByCartIdWithMenu(cart.getId());

        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(ci -> ci.getMenu() != null));
        assertTrue(found.stream().anyMatch(ci -> ci.getMenu().getName().equals("Menu 1")));
        assertTrue(found.stream().anyMatch(ci -> ci.getMenu().getName().equals("Menu 2")));
    }

    @Test
    void findAllByCartIdWithMenu_ShouldReturnEmptyList_WhenNoItems() {
        Cart emptyCart = new Cart();
        emptyCart.setUserId(user.getId());
        entityManager.persist(emptyCart);

        List<CartItem> found = cartItemRepository.findAllByCartIdWithMenu(emptyCart.getId());

        assertTrue(found.isEmpty());
    }

    @Test
    void findAllByCartId_ShouldReturnCartItems_WhenExist() {
        List<CartItem> found = cartItemRepository.findAllByCartId(cart.getId());

        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(ci -> ci.getCartId().equals(cart.getId())));
    }

    @Test
    void findAllByCartId_ShouldReturnEmptyList_WhenNoItems() {
        Cart emptyCart = new Cart();
        emptyCart.setUserId(user.getId());
        entityManager.persist(emptyCart);

        List<CartItem> found = cartItemRepository.findAllByCartId(emptyCart.getId());

        assertTrue(found.isEmpty());
    }
}