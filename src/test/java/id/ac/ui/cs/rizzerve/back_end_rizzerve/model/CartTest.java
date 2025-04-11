package id.ac.ui.cs.rizzerve.back_end_rizzerve.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {
    private Cart cart;
    private User user;
    private CartItem cartItem;
    private Product product;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("TestUser")
                .build();

        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .build();

        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
    }

    @Test
    void testGetId() {
        assertEquals(1L, cart.getId());
    }

    @Test
    void testGetUser() {
        assertEquals(user, cart.getUser());
    }

    @Test
    void testGetItems() {
        assertEquals(1, cart.getItems().size());
        assertEquals(cartItem, cart.getItems().get(0));
    }

    @Test
    void testSetId() {
        cart.setId(2L);
        assertEquals(2L, cart.getId());
    }

    @Test
    void testSetUser() {
        User newUser = User.builder()
                .id(2L)
                .name("New User")
                .build();
        cart.setUser(newUser);
        assertEquals(newUser, cart.getUser());
    }

    @Test
    void testSetItems() {
        List<CartItem> newItems = new ArrayList<>();
        Product newProduct = Product.builder()
                .id(2L)
                .name("New Product")
                .build();
        CartItem newCartItem = CartItem.builder()
                .id(2L)
                .product(newProduct)
                .quantity(3)
                .build();
        newItems.add(newCartItem);

        cart.setItems(newItems);
        assertEquals(1, cart.getItems().size());
        assertEquals(newCartItem, cart.getItems().get(0));
    }
}