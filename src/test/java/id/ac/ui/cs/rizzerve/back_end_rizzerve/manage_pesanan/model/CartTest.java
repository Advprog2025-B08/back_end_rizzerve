package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;
    private User user;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();

        cart = Cart.builder()
                .id(1L)
                .userId(1L)
                .user(user)
                .items(new ArrayList<>())
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .menuId(1L)
                .cartId(1L)
                .quantity(2)
                .cart(cart)
                .build();
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, cart.getId());
        assertEquals(1L, cart.getUserId());
        assertEquals(user, cart.getUser());
        assertTrue(cart.getItems().isEmpty());

        // Test setters
        cart.setId(2L);
        cart.setUserId(2L);

        User newUser = User.builder().id(2L).username("newuser").password("newpass").build();
        cart.setUser(newUser);

        List<CartItem> newItems = new ArrayList<>();
        newItems.add(cartItem);
        cart.setItems(newItems);

        assertEquals(2L, cart.getId());
        assertEquals(2L, cart.getUserId());
        assertEquals(newUser, cart.getUser());
        assertEquals(1, cart.getItems().size());
        assertEquals(cartItem, cart.getItems().get(0));
    }

    @Test
    void testAddingAndRemovingItems() {
        // Add item to cart
        cart.getItems().add(cartItem);
        assertEquals(1, cart.getItems().size());

        // Remove item from cart
        cart.getItems().remove(cartItem);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testNoArgsConstructor() {
        Cart emptyCart = new Cart();
        assertNull(emptyCart.getId());
        assertNull(emptyCart.getUserId());
        assertNull(emptyCart.getUser());
        assertNotNull(emptyCart.getItems());
        assertTrue(emptyCart.getItems().isEmpty());
    }

    @Test
    void testAllArgsConstructor() {
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);

        Cart fullCart = new Cart(1L, 1L, user, items);

        assertEquals(1L, fullCart.getId());
        assertEquals(1L, fullCart.getUserId());
        assertEquals(user, fullCart.getUser());
        assertEquals(1, fullCart.getItems().size());
        assertEquals(cartItem, fullCart.getItems().get(0));
    }
}