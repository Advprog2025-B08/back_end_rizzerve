package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CartTest {

    private Cart cart;
    private User user;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");

        cart = Cart.builder()
                .id(1L)
                .userId(1L)
                .user(user)
                .items(new ArrayList<>())
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .menuId(101L)
                .quantity(2)
                .cartId(1L)
                .build();
    }

    @Test
    void testCartCreation() {
        assertNotNull(cart);
        assertEquals(1L, cart.getId());
        assertEquals(1L, cart.getUserId());
        assertEquals(user, cart.getUser());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testAddItemToCart() {
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);

        assertEquals(1, cart.getItems().size());
        assertEquals(cartItem, cart.getItems().get(0));
    }

    @Test
    void testRemoveItemFromCart() {
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);

        cart.getItems().remove(cartItem);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testCartUserAssociation() {
        assertEquals(user, cart.getUser());
        assertEquals("testuser", cart.getUser().getUsername());
    }
}