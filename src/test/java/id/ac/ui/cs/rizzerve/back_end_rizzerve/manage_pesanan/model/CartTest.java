package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");

        cart = Cart.builder()
                .id(1L)
                .userId(user.getId())
                .user(user)
                .items(new ArrayList<>())
                .build();
    }

    @Test
    void testCartCreation() {
        assertNotNull(cart);
        assertEquals(1L, cart.getId());
        assertEquals(1L, cart.getUserId());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testCartUserRelationship() {
        assertEquals(user, cart.getUser());
        assertEquals(user.getId(), cart.getUserId());
    }

    @Test
    void testCartItemsRelationship() {
        CartItem item1 = new CartItem();
        item1.setId(1L);
        item1.setQuantity(2);
        item1.setCartId(cart.getId());

        CartItem item2 = new CartItem();
        item2.setId(2L);
        item2.setQuantity(1);
        item2.setCartId(cart.getId());

        List<CartItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        cart.setItems(items);

        assertEquals(2, cart.getItems().size());
        assertEquals(1L, cart.getItems().get(0).getCartId());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testCartLombokAnnotations() {
        Cart newCart = new Cart();
        newCart.setId(2L);
        newCart.setUserId(2L);
        newCart.setUser(user);
        newCart.setItems(new ArrayList<>());

        assertEquals(2L, newCart.getId());
        assertEquals(2L, newCart.getUserId());
        assertEquals(user, newCart.getUser());
        assertNotNull(newCart.getItems());
    }

    @Test
    void testCartBuilder() {
        Cart builtCart = Cart.builder()
                .id(3L)
                .userId(3L)
                .user(user)
                .items(new ArrayList<>())
                .build();

        assertEquals(3L, builtCart.getId());
        assertEquals(3L, builtCart.getUserId());
        assertEquals(user, builtCart.getUser());
        assertNotNull(builtCart.getItems());
    }
}